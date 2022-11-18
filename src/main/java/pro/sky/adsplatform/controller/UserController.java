package pro.sky.adsplatform.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pro.sky.adsplatform.dto.*;
import pro.sky.adsplatform.entity.AdsEntity;
import pro.sky.adsplatform.entity.UserEntity;
import pro.sky.adsplatform.exception.NotFoundException;
import pro.sky.adsplatform.mapper.ResponseWrapperUserMapper;
import pro.sky.adsplatform.mapper.UserMapper;
import pro.sky.adsplatform.service.AuthService;
import pro.sky.adsplatform.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserMapper userMapper;
    private final ResponseWrapperUserMapper responseWrapperUserMapper;
    private final AuthService authService;
    private final UserService userService;
    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public UserController(UserMapper userMapper,
                          ResponseWrapperUserMapper responseWrapperUserMapper,
                          AuthService authService,
                          UserService userService,
                          HttpServletRequest request) {
        this.userMapper = userMapper;
        this.responseWrapperUserMapper = responseWrapperUserMapper;
        this.authService = authService;
        this.userService = userService;
        this.request = request;
    }

    /**
     * Возвращает список всех пользователей.
     */
    @Operation(
            summary = "getUsers",
            description = "Получить список пользователей",
            tags = {"Пользователи"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    @GetMapping("/me")
    public ResponseEntity<ResponseWrapperUserDto> getUsers() {

        List<UserEntity> userList = userService.findAllUsers();
        if (userList.size() == 0) {
            return new ResponseEntity<ResponseWrapperUserDto>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(responseWrapperUserMapper
                .userListToResponseWrapperUserDto(userList.size(), userList));

    }


    /**
     * Обновляет данные пользователя.
     */
    @Operation(
            summary = "updateUser",
            description = "Обновить данные пользователя",
            tags = {"Пользователи"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @PatchMapping("/me")
    public ResponseEntity<UserDto> updateUser(Authentication authentication, @RequestBody UserDto userDto) {
        userDto.setEmail(authentication.getName());
        userDto.setId(Math.toIntExact(userService.findUserByName(authentication.getName()).getId()));
        UserEntity user = userMapper.userDtoToUser(userDto);
        try {
            userService.updateUser(user);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(userDto);
    }

    /**
     * Обновляет пароль.
     */
    @Operation(
            summary = "setPassword",
            description = "Обновить пароль пользователя",
            tags = {"Пользователи"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "201", description = "Created"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    @PostMapping(value = "/set_password")
    public ResponseEntity<NewPasswordDto> setPassword(Authentication authentication,@RequestBody NewPasswordDto newPasswordDto) {
        authService.changePassword(authentication,newPasswordDto.getCurrentPassword(), newPasswordDto.getNewPassword());
        return ResponseEntity.ok(newPasswordDto);
    }

    /**
     * Возвращает пользователя по его ID.
     */
    @Operation(
            summary = "getUser",
            description = "Получить данные о пользователе по его ID",
            tags = {"Пользователи"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    @GetMapping(path = "{id}")
    public ResponseEntity<UserDto> getUser(Authentication authentication, @PathVariable("id") Integer id) {
        UserEntity user = userService.findUser(id);

        if (!user.getUsername().equals(authentication.getName()))
            return new ResponseEntity<UserDto>(HttpStatus.FORBIDDEN);

        if (user == null) {
            return new ResponseEntity<UserDto>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(userMapper.userToUserDto(user));
    }
}
