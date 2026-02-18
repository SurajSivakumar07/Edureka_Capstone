package com.fytzi.inventoryservice.dto;

public record CreateOrUpdateInventoryRequest(
        Long productId,
        Integer availableQty
) {}
