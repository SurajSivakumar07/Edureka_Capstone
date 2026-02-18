package com.fytzi.userservice.service;

import com.fytzi.userservice.dto.UserRegisterResponse;
import com.fytzi.userservice.dto.UserResponse;
import com.fytzi.userservice.entity.User;

public interface UserService {

    UserResponse getByEmail(String name);

    UserRegisterResponse create(User user);
}