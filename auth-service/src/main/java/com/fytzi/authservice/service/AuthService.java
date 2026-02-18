package com.fytzi.authservice.service;

import com.fytzi.authservice.dto.LoginRequest;
import com.fytzi.authservice.dto.LoginResponse;

public interface AuthService {
    public LoginResponse login(LoginRequest request);
}
