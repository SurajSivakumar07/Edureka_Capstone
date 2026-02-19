package com.fytzi.apigateway.exception;

import com.fytzi.apigateway.dto.ErrorResponse;
import org.apache.http.protocol.HTTP;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(UserNotAuthorized.class)
    public ResponseEntity<ErrorResponse> handleUnauthorised(String messeage){

        ErrorResponse errorResponse=ErrorResponse.builder()
                .message(messeage)
                .status(401).build();

        return ResponseEntity.ok().body(errorResponse);
    }
}
