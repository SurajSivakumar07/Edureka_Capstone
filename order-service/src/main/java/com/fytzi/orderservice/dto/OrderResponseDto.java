package com.fytzi.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponseDto {
        private Long orderId;
        private Long userId;
        private String status;
        private BigDecimal totalAmount;
        private List<OrderItemResponseDto> items;
}
