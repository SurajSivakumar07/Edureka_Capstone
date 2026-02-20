package com.fytzi.orderservice.dto;

public record CreateOrderItemRequest(
        long productId,
        int quantity
) {}
