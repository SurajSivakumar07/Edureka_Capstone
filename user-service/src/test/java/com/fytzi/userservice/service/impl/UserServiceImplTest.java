package com.fytzi.userservice.service.impl;

import com.fytzi.userservice.entity.User;
import com.fytzi.userservice.exception.InvalidRoleException;
import com.fytzi.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User validUser;

    @BeforeEach
    void setUp() {
        validUser = User.builder()
                .email("test@example.com")
                .password("password123")
                .role("USER")
                .build();
    }

    @Test
    void create_WithValidUserRole_ShouldSucceed() {
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");

        userService.create(validUser);

        verify(userRepository, times(1)).save(validUser);
    }

    @Test
    void create_WithValidAdminRole_ShouldSucceed() {
        validUser.setRole("ADMIN");
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");

        userService.create(validUser);

        verify(userRepository, times(1)).save(validUser);
    }

    @Test
    void create_WithInvalidRole_ShouldThrowInvalidRoleException() {
        validUser.setRole("GUEST");
        when(userRepository.existsByEmail(any())).thenReturn(false);

        assertThrows(InvalidRoleException.class, () -> userService.create(validUser));
        verify(userRepository, never()).save(any());
    }

    @Test
    void create_WithNullRole_ShouldThrowInvalidRoleException() {
        validUser.setRole(null);
        when(userRepository.existsByEmail(any())).thenReturn(false);

        assertThrows(InvalidRoleException.class, () -> userService.create(validUser));
        verify(userRepository, never()).save(any());
    }

    @Test
    void create_WithLowercaseRole_ShouldSucceed() {
        validUser.setRole("user");
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");

        userService.create(validUser);

        verify(userRepository, times(1)).save(validUser);
    }
}
