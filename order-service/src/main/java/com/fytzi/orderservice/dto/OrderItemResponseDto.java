package com.fytzi.orderservice.dto;

import java.math.BigDecimal;

public record OrderItemResponseDto(
        Long productId,
        Integer quantity,
        BigDecimal priceAtPurchase
) {}
