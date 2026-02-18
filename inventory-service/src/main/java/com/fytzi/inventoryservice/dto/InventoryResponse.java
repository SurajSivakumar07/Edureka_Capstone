package com.fytzi.inventoryservice.dto;

public record InventoryResponse(
        Long productId,
        Integer availableQty,
        Integer reservedQty,
        Boolean inStock
) {}
