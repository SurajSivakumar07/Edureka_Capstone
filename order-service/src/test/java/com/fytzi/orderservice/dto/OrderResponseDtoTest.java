package com.fytzi.orderservice.dto;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class OrderResponseDtoTest {

    @Test
    void testOrderResponseDtoBuilderAndGetters() {
        OrderItemResponseDto item = OrderItemResponseDto.builder()
                .productId(101L)
                .quantity(2)
                .priceAtPurchase(new BigDecimal("50.00"))
                .build();

        OrderResponseDto dto = OrderResponseDto.builder()
                .orderId(1L)
                .userId(1L)
                .status("CREATED")
                .totalAmount(new BigDecimal("100.00"))
                .items(Collections.singletonList(item))
                .build();

        assertEquals(1L, dto.getOrderId());
        assertEquals(1L, dto.getUserId());
        assertEquals("CREATED", dto.getStatus());
        assertEquals(new BigDecimal("100.00"), dto.getTotalAmount());
        assertEquals(1, dto.getItems().size());
        assertEquals(101L, dto.getItems().get(0).getProductId());
    }

    @Test
    void testNoArgsConstructor() {
        OrderResponseDto dto = new OrderResponseDto();
        assertNull(dto.getOrderId());
        assertNull(dto.getItems());
    }
}
