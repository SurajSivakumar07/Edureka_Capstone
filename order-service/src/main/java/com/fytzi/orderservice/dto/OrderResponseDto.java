package com.fytzi.orderservice.dto;

import java.math.BigDecimal;
import java.util.List;

public record OrderResponseDto(
        Long orderId,
        Long userId,
        String status,
        BigDecimal totalAmount,
        List<OrderItemResponseDto> items
) {}
