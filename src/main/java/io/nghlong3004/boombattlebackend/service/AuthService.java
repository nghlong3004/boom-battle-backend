package io.nghlong3004.boombattlebackend.service;

import io.nghlong3004.boombattlebackend.model.AuthResponse;
import io.nghlong3004.boombattlebackend.model.LoginRequest;
import io.nghlong3004.boombattlebackend.model.RegisterRequest;

public interface AuthService {

    AuthResponse login(LoginRequest loginRequest);

    String register(RegisterRequest registerRequest);

}
