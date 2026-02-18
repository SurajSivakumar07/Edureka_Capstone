package com.fytzi.authservice.client;


import com.fytzi.authservice.dto.InternalUserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/users/by-email")
    InternalUserResponse getUserInternal(@RequestParam("email") String email);
}