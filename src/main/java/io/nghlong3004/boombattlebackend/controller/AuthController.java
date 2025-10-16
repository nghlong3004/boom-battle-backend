package io.nghlong3004.boombattlebackend.controller;

import io.nghlong3004.boombattlebackend.model.AuthResponse;
import io.nghlong3004.boombattlebackend.model.LoginRequest;
import io.nghlong3004.boombattlebackend.model.RegisterRequest;
import io.nghlong3004.boombattlebackend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @ResponseStatus(code = HttpStatus.OK)
    public AuthResponse authenticateUser(
            @Validated
            @RequestBody
            LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/register")
    @ResponseStatus(code = HttpStatus.OK)
    public void registerUser(
            @RequestBody RegisterRequest registerRequest) {
        authService.register(registerRequest);
    }
}
