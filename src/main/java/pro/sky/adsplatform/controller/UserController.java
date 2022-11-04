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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public UserController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
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
        log.debug("ResponseWrapperUserDto is running");
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<ResponseWrapperUserDto>(objectMapper.readValue("{\n  \"count\" : 0,\n  \"results\" : [ {\n    \"firstName\" : \"firstName\",\n    \"lastName\" : \"lastName\",\n    \"phone\" : \"phone\",\n    \"id\" : 6,\n    \"email\" : \"email\"\n  }, {\n    \"firstName\" : \"firstName\",\n    \"lastName\" : \"lastName\",\n    \"phone\" : \"phone\",\n    \"id\" : 6,\n    \"email\" : \"email\"\n  } ]\n}", ResponseWrapperUserDto.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<ResponseWrapperUserDto>(HttpStatus.INTERNAL_SERVER_ERROR);
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
        log.debug("updateUserUsingPATCH is running");
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<UserDto>(objectMapper.readValue("{\n  \"firstName\" : \"firstName\",\n  \"lastName\" : \"lastName\",\n  \"phone\" : \"phone\",\n  \"id\" : 6,\n  \"email\" : \"email\"\n}", UserDto.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<UserDto>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<UserDto>(HttpStatus.NOT_IMPLEMENTED);
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
        log.debug("setPasswordUsingPOST is running");
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<NewPasswordDto>(objectMapper.readValue("{\n  \"newPassword\" : \"newPassword\",\n  \"currentPassword\" : \"currentPassword\"\n}", NewPasswordDto.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
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
        log.debug("getUserUsingGET is running");
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<UserDto>(objectMapper.readValue("{\n  \"firstName\" : \"firstName\",\n  \"lastName\" : \"lastName\",\n  \"phone\" : \"phone\",\n  \"id\" : 6,\n  \"email\" : \"email\"\n}", UserDto.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<UserDto>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<UserDto>(HttpStatus.NOT_IMPLEMENTED);
    }
}
