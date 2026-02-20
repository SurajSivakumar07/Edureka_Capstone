package com.fytzi.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class InternalUserResponse {
    private Long userId;
    private String email;
    private String password;
    private String role;
}
