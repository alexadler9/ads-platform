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
import pro.sky.adsplatform.mapper.ResponseWrapperUserMapper;
import pro.sky.adsplatform.mapper.UserMapper;
import pro.sky.adsplatform.service.AuthService;
import pro.sky.adsplatform.service.UserService;

import javax.naming.NotContextException;
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
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен")
            }
    )
    @GetMapping("/me")
    public ResponseEntity<ResponseWrapperUserDto> getUsers() {
        LOGGER.info("Получение списка пользователей");

        List<UserEntity> userList = userService.findAllUsers();

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
    ) throws NotContextException {
        LOGGER.info("Обновление данных пользователя: {}", userDto);

        UserEntity user = userService.findUserContentByName(authentication.getName());
        userDto.setEmail(authentication.getName());
        userDto.setId(Math.toIntExact(user.getId()));

        UserEntity userUpdated = userService.updateUser(userMapper.userDtoToUser(userDto));

        return ResponseEntity.ok(userMapper.userToUserDto(userUpdated));
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
            Authentication authentication,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Параметры пароля")
            @RequestBody NewPasswordDto newPasswordDto
    ) {
        LOGGER.info("Обновление пароля пользователя: {}", newPasswordDto);

        authService.changePassword(
                authentication,
                newPasswordDto.getCurrentPassword(),
                newPasswordDto.getNewPassword()
        );

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
        LOGGER.info("Получение данных пользователя {}", id);

        UserEntity user = userService.findUser(id);
        if (!user.getUsername().equals(authentication.getName())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok(userMapper.userToUserDto(user));
    }
}
