package com.fytzi.orderservice.dto;

import java.util.List;

public record CreateOrderRequest(
        List<CreateOrderItemRequest> items
) {}
