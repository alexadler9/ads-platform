package pro.sky.adsplatform.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.adsplatform.dto.*;
import pro.sky.adsplatform.entity.AvatarEntity;
import pro.sky.adsplatform.entity.UserEntity;
import pro.sky.adsplatform.mapper.ResponseWrapperUserMapper;
import pro.sky.adsplatform.mapper.UserMapper;
import pro.sky.adsplatform.service.AuthService;
import pro.sky.adsplatform.service.AvatarService;
import pro.sky.adsplatform.service.UserService;

import javax.validation.Valid;


@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserMapper userMapper;
    private final AuthService authService;
    private final UserService userService;
    private final AvatarService avatarService;

    @Autowired
    public UserController(UserMapper userMapper,
                          AuthService authService,
                          UserService userService,
                          AvatarService avatarService) {
        this.userMapper = userMapper;
        this.authService = authService;
        this.userService = userService;
        this.avatarService = avatarService;
    }

    /**
     * Возвращает данные текущего пользователя.
     */
    @Operation(
            summary = "Получить данные текущего пользователя",
            tags = {"Пользователи"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Данные текущего пользователя успешно получены"),
                    @ApiResponse(responseCode = "401", description = "Требуется авторизация"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
                    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
            }
    )
    @GetMapping("/me")
    public ResponseEntity<UserDto> meUser(Authentication authentication) {
        LOGGER.info("Получение данных пользователя {}", authentication.getName());

        return ResponseEntity.ok(userMapper.userToUserDto(userService.findUserByName(authentication.getName())));
    }

    /**
     * Обновляет данные текущего пользователя.
     */
    @Operation(
            summary = "Обновить данные текущего пользователя",
            tags = {"Пользователи"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Данные текущего пользователя успешно обновлены"),
                    @ApiResponse(responseCode = "401", description = "Требуется авторизация"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
                    @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            }
    )
    @PatchMapping("/me")
    public ResponseEntity<UserDto> updateUser(
            Authentication authentication,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Параметры пользователя")
            @RequestBody @Valid UserDto userDto
    ) {
        LOGGER.info("Обновление данных пользователя {}: {}", authentication.getName(), userDto);

        UserEntity user = userService.findUserByName(authentication.getName());
        userDto.setEmail(authentication.getName());
        userDto.setId(Math.toIntExact(user.getId()));

        UserEntity userUpdated = userService.updateUser(userMapper.userDtoToUser(userDto));

        return ResponseEntity.ok(userMapper.userToUserDto(userUpdated));
    }

    /**
     * Обновляет изображение текущего пользователя.
     */
    @Operation(
            summary = "Обновить изображение текущего пользователя",
            tags = {"Пользователи"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Изображение успешно обновлено"),
                    @ApiResponse(responseCode = "401", description = "Требуется авторизация"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
                    @ApiResponse(responseCode = "404", description = "Пользователь и/или содержимое изображения не найдено")
            }
    )
    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateUserImage(
            Authentication authentication,
            @Parameter(description = "Изображение")
            @RequestParam MultipartFile image
    ) {
        LOGGER.info("Обновление изображения пользователя {}", authentication.getName());

        UserEntity user = userService.findUserByName(authentication.getName());
        avatarService.updateAvatar(user, image);

        return ResponseEntity.ok().build();
    }

    /**
     * Возвращает изображение текущего пользователя.
     */
    @Operation(
            summary = "Получить изображение текущего пользователя",
            tags = {"Пользователи"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Изображение успешно получено"),
                    @ApiResponse(responseCode = "401", description = "Требуется авторизация"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
                    @ApiResponse(responseCode = "404", description = "Пользователь и/или изображение не найдено")
            }
    )
    @GetMapping(value = "/me/image")
    public ResponseEntity<byte[]> getUserImage(
            Authentication authentication
    ) {
        LOGGER.info("Получение изображения пользователя {}", authentication.getName());

        UserEntity user = userService.findUserByName(authentication.getName());
        AvatarEntity image = avatarService.findAvatar(user.getId());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType("image/jpeg"));
        httpHeaders.setContentLength(image.getImage().length);

        return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(image.getImage());
    }

    /**
     * Обновляет пароль.
     */
    @Operation(
            summary = "Обновить пароль текущего пользователя",
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
            @RequestBody @Valid NewPasswordDto newPasswordDto
    ) {
        LOGGER.info("Обновление пароля пользователя {}: {}", authentication.getName(), newPasswordDto);

        authService.changePassword(
                authentication,
                newPasswordDto.getCurrentPassword(),
                newPasswordDto.getNewPassword()
        );

        return ResponseEntity.ok(newPasswordDto);
    }

    /**
     * Возвращает данные пользователя по его ID.
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
