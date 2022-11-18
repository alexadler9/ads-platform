package pro.sky.adsplatform.controller;

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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginReqDto req) {
        if (authService.login(req.getUsername(), req.getPassword())) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterReqDto req) {
        RoleDto role = (req.getRole() == null) ? USER : req.getRole();
        if (authService.register(req, role)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/meuser")
    public ResponseEntity<?> meUser(Authentication authentication) {
        String au = authentication.getName();
        return ResponseEntity.ok(authentication);
    }
}


