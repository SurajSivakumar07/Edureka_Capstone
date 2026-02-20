package com.fytzi.inventoryservice.dto;

public record CreateOrUpdateInventory(
                Long productId,
                Integer availableQty) {
}
