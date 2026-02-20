package com.fytzi.orderservice.dto;

import java.math.BigDecimal;

public record ProductDto(Long id, String name, BigDecimal price) {
}
