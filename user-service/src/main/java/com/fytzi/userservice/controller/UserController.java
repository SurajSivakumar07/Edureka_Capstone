package com.fytzi.userservice.controller;

import com.fytzi.userservice.dto.UserRegisterResponse;
import com.fytzi.userservice.dto.UserResponse;
import com.fytzi.userservice.entity.User;
import com.fytzi.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User Management", description = "Registering and searching for platform users")
public class UserController {

    private final UserService userService;

    /**
     * Register a new user
     */
    @PostMapping("/create")
    @Operation(summary = "Register a user", description = "Creates a new user profile with encrypted credentials")
    public ResponseEntity<UserRegisterResponse> register(@RequestBody @Valid User user) {
        UserRegisterResponse response = userService.create(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    /**
     * Fetch user by email
     */

    @GetMapping("/by-email")
    @Operation(summary = "Find user by email", description = "Look up user details via their unique email address")
    public ResponseEntity<UserResponse> getByEmail(@RequestParam("email") String email) {
        log.info("📥 Received request for email: {}", email);
        UserResponse response = userService.getByEmail(email);
        return ResponseEntity.ok(response);
    }
}