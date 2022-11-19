package pro.sky.adsplatform.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pro.sky.adsplatform.dto.LoginReqDto;
import pro.sky.adsplatform.dto.RegisterReqDto;
import pro.sky.adsplatform.dto.RoleDto;
import pro.sky.adsplatform.service.AuthService;

import static pro.sky.adsplatform.dto.RoleDto.USER;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(
            summary = "Авторизовать пользователя",
            tags = {"Авторизация"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Авторизация успешно выполнена"),
                    @ApiResponse(responseCode = "403", description = "Не удалось выполнить авторизацию")
            }
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Параметры авторизации")
            @RequestBody LoginReqDto loginReqDto
    ) {
        if (authService.login(loginReqDto.getUsername(), loginReqDto.getPassword())) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Operation(
            summary = "Зарегистрировать пользователя",
            tags = {"Авторизация"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Регистрация успешно выполнена"),
                    @ApiResponse(responseCode = "400", description = "Не удалось выполнить регистрацию")
            }
    )
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Параметры регистрации")
            @RequestBody RegisterReqDto registerReqDto
    ) {

        if (authService.register(registerReqDto, USER)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @Operation(
            summary = "meUser",
            tags = {"Авторизация"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK")
            }
    )
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/meuser")
    public ResponseEntity<?> meUser(Authentication authentication) {
        String au = authentication.getName();
        return ResponseEntity.ok(authentication);
    }
}


