package com.fytzi.orderservice.exception;

import com.fytzi.orderservice.dto.ErrorResponse;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice   // better than @ControllerAdvice for REST APIs
public class GlobalException {

    @ExceptionHandler(InvalidProductIdException.class)
    public ResponseEntity<ErrorResponse> handleInvalidProduct(InvalidProductIdException ex) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    @ExceptionHandler(InsufficeintQuantityException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientQuantity(InsufficeintQuantityException ex) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(400)
                .message("Invalid product id")
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}