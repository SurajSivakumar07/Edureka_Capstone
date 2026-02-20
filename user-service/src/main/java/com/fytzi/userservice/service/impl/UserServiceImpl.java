package com.fytzi.userservice.service.impl;

import com.fytzi.userservice.dto.UserRegisterResponse;
import com.fytzi.userservice.dto.UserResponse;
import com.fytzi.userservice.entity.User;
import com.fytzi.userservice.exception.UserException;
import com.fytzi.userservice.exception.UserNotFoundException;
import com.fytzi.userservice.repository.UserRepository;
import com.fytzi.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse getByEmail(String email) {
        User user=userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Invalid username or password"));

        return UserResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .build();
    }

    @Override
    public UserRegisterResponse create(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserException("User already exists with email: " + user.getEmail());
        }

        // 🔐 Hash password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);


        return UserRegisterResponse.builder()
                .email(user.getEmail())
                .role(user.getRole())
                .message("User registered successfully. Please login.")
                .build();

    }
}
