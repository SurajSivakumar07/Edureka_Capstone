package com.fytzi.orderservice.controller;

import com.fytzi.orderservice.dto.CreateOrderRequest;
import com.fytzi.orderservice.dto.OrderResponseDto;
import com.fytzi.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<OrderResponseDto> create(
            @RequestHeader("X-USER-ID") String userId, @RequestBody CreateOrderRequest request) {

        log.info("userId  value: [{}]", userId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.createOrder(request, userId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponseDto>> getUserOrders(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getOrdersByUser(userId));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
}
