package com.fytzi.userservice.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRegisterResponse {
    private String email;
    private String role;
    private String message;
}