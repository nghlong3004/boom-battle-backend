package io.nghlong3004.boombattlebackend.service.impl;

import io.nghlong3004.boombattlebackend.exception.ErrorCode;
import io.nghlong3004.boombattlebackend.exception.ResourceException;
import io.nghlong3004.boombattlebackend.model.AuthResponse;
import io.nghlong3004.boombattlebackend.model.Boomber;
import io.nghlong3004.boombattlebackend.model.LoginRequest;
import io.nghlong3004.boombattlebackend.model.RegisterRequest;
import io.nghlong3004.boombattlebackend.repository.UserRepository;
import io.nghlong3004.boombattlebackend.security.JwtTokenProvider;
import io.nghlong3004.boombattlebackend.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    private final JwtTokenProvider tokenProvider;

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));

        SecurityContextHolder.getContext()
                             .setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        return new AuthResponse(jwt);
    }

    @Override
    public String register(RegisterRequest registerRequest) {
        userRepository.existsByUsername(registerRequest.username())
                      .orElseThrow(() -> new ResourceException(ErrorCode.USER_EXISTS));

        Boomber user = new Boomber();
        user.setUsername(registerRequest.username());
        user.setPassword(encoder.encode(registerRequest.password()));

        userRepository.save(user);

        return "User registered successfully!";
    }
}
