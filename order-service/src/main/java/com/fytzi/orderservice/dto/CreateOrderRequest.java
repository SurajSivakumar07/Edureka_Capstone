package com.fytzi.orderservice.dto;

import java.util.List;

public record CreateOrderRequest(
        Long userId,
        List<CreateOrderItemRequest> items
) {}
