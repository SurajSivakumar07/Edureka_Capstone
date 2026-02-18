package com.fytzi.orderservice.dto;

public record CreateOrderItemRequest(
        Long productId,
        Integer quantity
) {}
