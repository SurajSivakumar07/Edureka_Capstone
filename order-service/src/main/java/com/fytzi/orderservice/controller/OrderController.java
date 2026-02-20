package com.fytzi.orderservice.controller;

import com.fytzi.orderservice.dto.CreateOrderRequest;
import com.fytzi.orderservice.dto.OrderResponseDto;
import com.fytzi.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Order Management", description = "Processing and managing customer orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    @Operation(summary = "Place a new order", description = "Validates items and creates a new order for a user")
    public ResponseEntity<OrderResponseDto> create(
            @RequestHeader("X-USER-ID") String userId, @RequestBody CreateOrderRequest request) {

        log.info("userId  value: [{}]", userId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.createOrder(request, userId));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "List orders by user", description = "Retrieves all orders associated with a specific user ID")
    public ResponseEntity<List<OrderResponseDto>> getUserOrders(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getOrdersByUser(userId));
    }

    @GetMapping
    @Operation(summary = "List all orders", description = "Retrieves every order in the system (Admin only typically)")
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
}
