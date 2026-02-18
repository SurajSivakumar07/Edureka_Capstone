package com.fytzi.inventoryservice.dto;

public record ReleaseInventoryRequest(
        Long productId,
        Integer quantity
) {}
