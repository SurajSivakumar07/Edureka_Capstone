package com.fytzi.inventoryservice.dto;

public record ReserveInventoryRequest(
        Long productId,
        Integer quantity
) {}
