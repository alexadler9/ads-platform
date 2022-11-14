package pro.sky.adsplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.adsplatform.dto.*;
import pro.sky.adsplatform.entity.UserEntity;
import pro.sky.adsplatform.exception.NotFoundException;
import pro.sky.adsplatform.mapper.ResponseWrapperUserMapper;
import pro.sky.adsplatform.mapper.UserMapper;
import pro.sky.adsplatform.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;


@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserMapper userMapper;
    private final ResponseWrapperUserMapper responseWrapperUserMapper;
    private final ObjectMapper objectMapper;

    private final UserService userService;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public UserController(UserMapper userMapper,
                          ResponseWrapperUserMapper responseWrapperUserMapper,
                          ObjectMapper objectMapper,
                          UserService userService,
                          HttpServletRequest request) {
        this.userMapper = userMapper;
        this.responseWrapperUserMapper = responseWrapperUserMapper;
        this.objectMapper = objectMapper;
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
    public ResponseEntity<ResponseWrapperUserDto> getUsersUsingGET() {
        LOGGER.debug("ResponseWrapperUserDto is running");

        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            List<UserEntity> userEntities = userService.getAllUsers();
            Integer count = userEntities.size();
            if (userEntities.size() > 0) {
                ResponseWrapperUserDto responseWrapperUserDto = responseWrapperUserMapper
                        .userListToResponseWrapperUserDto(count, userEntities);
                return ResponseEntity.ok(responseWrapperUserDto);
            } else {
                return new ResponseEntity<ResponseWrapperUserDto>(HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<ResponseWrapperUserDto>(HttpStatus.NOT_IMPLEMENTED);
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
    public ResponseEntity<UserDto> updateUserUsingPATCH(@Parameter(in = ParameterIn.DEFAULT, description = "user", required = true, schema = @Schema()) @Valid @RequestBody UserDto body) {
        UserEntity userEntity = userMapper.userDtoToUser(body);
        try {
            userService.updateUser(userEntity);
        } catch (NotFoundException e) {
            return new ResponseEntity<UserDto>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(body);
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
    @PostMapping("/setPassword")
    public ResponseEntity<NewPasswordDto> setPasswordUsingPOST(@Parameter(in = ParameterIn.DEFAULT, description = "newPassword", required = true, schema = @Schema()) @Valid @RequestBody NewPasswordDto body) {
        LOGGER.debug("setPasswordUsingPOST is running");
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<NewPasswordDto>(objectMapper.readValue("{\n  \"newPassword\" : \"newPassword\",\n  \"currentPassword\" : \"currentPassword\"\n}", NewPasswordDto.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                LOGGER.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<NewPasswordDto>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<NewPasswordDto>(HttpStatus.NOT_IMPLEMENTED);
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
    public ResponseEntity<UserDto> getUserUsingGET(@Parameter(in = ParameterIn.PATH, description = "id", required = true, schema = @Schema()) @PathVariable("id") Integer id) {
        UserEntity user = userService.getUser(id);
        if (user == null) {
            return new ResponseEntity<UserDto>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(userMapper.userToUserDto(user));
    }
}
