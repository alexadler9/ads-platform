package pro.sky.adsplatform.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pro.sky.adsplatform.dto.*;
import pro.sky.adsplatform.entity.UserEntity;
import pro.sky.adsplatform.exception.NotFoundException;
import pro.sky.adsplatform.mapper.ResponseWrapperUserMapper;
import pro.sky.adsplatform.mapper.UserMapper;
import pro.sky.adsplatform.service.AuthService;
import pro.sky.adsplatform.service.UserService;

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

    @Autowired
    public UserController(UserMapper userMapper,
                          ResponseWrapperUserMapper responseWrapperUserMapper,
                          AuthService authService,
                          UserService userService) {
        this.userMapper = userMapper;
        this.responseWrapperUserMapper = responseWrapperUserMapper;
        this.authService = authService;
        this.userService = userService;
    }

    /**
     * Возвращает список всех пользователей.
     */
    @Operation(
            summary = "Получить список пользователей",
            tags = {"Пользователи"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список успешно получен"),
                    @ApiResponse(responseCode = "401", description = "Требуется авторизация"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
                    @ApiResponse(responseCode = "404", description = "Список пуст")
            }
    )
    @GetMapping("/me")
    public ResponseEntity<ResponseWrapperUserDto> getUsers() {
        List<UserEntity> userList = userService.findAllUsers();
        if (userList.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(responseWrapperUserMapper
                .userListToResponseWrapperUserDto(userList.size(), userList));
    }

    /**
     * Обновляет данные пользователя.
     */
    @Operation(
            summary = "Обновить данные пользователя",
            tags = {"Пользователи"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Данные пользователя успешно обновлены"),
                    @ApiResponse(responseCode = "204", description = "Пользователь не найден"),
                    @ApiResponse(responseCode = "401", description = "Требуется авторизация"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен")
            }
    )
    @PatchMapping("/me")
    public ResponseEntity<UserDto> updateUser(
            Authentication authentication,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Параметры пользователя")
            @RequestBody UserDto userDto
    ) {
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
            summary = "Обновить пароль пользователя",
            tags = {"Пользователи"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Пароль успешно обновлен"),
                    @ApiResponse(responseCode = "401", description = "Требуется авторизация"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен")
            }
    )
    @PostMapping(value = "/set_password")
    public ResponseEntity<NewPasswordDto> setPassword(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Параметры пароля")
            @RequestBody NewPasswordDto newPasswordDto
    ) {
        authService.changePassword(newPasswordDto.getCurrentPassword(), newPasswordDto.getNewPassword());
        return ResponseEntity.ok(newPasswordDto);
    }

    /**
     * Возвращает пользователя по его ID.
     */
    @Operation(
            summary = "Получить данные пользователя по ID",
            tags = {"Пользователи"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Данные пользователя успешно получены"),
                    @ApiResponse(responseCode = "401", description = "Требуется авторизация"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
                    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
            }
    )
    @GetMapping(path = "{id}")
    public ResponseEntity<UserDto> getUser(
            Authentication authentication,
            @Parameter(description = "ID пользователя")
            @PathVariable("id") Integer id
    ) {
        UserEntity user = userService.findUser(id);
        if (user == null) {
            return new ResponseEntity<UserDto>(HttpStatus.NOT_FOUND);
        }

        if (!user.getUsername().equals(authentication.getName())) {
            return new ResponseEntity<UserDto>(HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok(userMapper.userToUserDto(user));
    }
}
