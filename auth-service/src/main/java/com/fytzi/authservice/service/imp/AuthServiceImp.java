package com.fytzi.authservice.service.imp;

import com.fytzi.authservice.client.UserClient;
import com.fytzi.authservice.dto.InternalUserResponse;
import com.fytzi.authservice.dto.LoginRequest;
import com.fytzi.authservice.dto.LoginResponse;
import com.fytzi.authservice.exception.UserNotFoundException;
import com.fytzi.authservice.service.AuthService;
import com.fytzi.authservice.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImp implements AuthService {

    private final UserClient userClient;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public LoginResponse login(LoginRequest request) {
        InternalUserResponse user = userClient.getUserInternal(request.getEmail());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UserNotFoundException("Invalid username or password");
        }

        String token = jwtUtil.generateToken(
                String.valueOf(user.getUserId()),
                user.getRole()
        );

        return new LoginResponse(token);
    }
}