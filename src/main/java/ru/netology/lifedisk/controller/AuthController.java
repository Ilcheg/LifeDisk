package ru.netology.lifedisk.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.netology.lifedisk.dto.JwtRequest;
import ru.netology.lifedisk.service.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JwtRequest authRequest) {
        return authService.login(authRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("auth-token") String authToken) {
        authService.logout(authToken);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
