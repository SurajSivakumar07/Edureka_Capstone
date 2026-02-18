package com.fytzi.inventoryservice.exception;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(Long productId, int requestedQty) {
        super("Insufficient stock for productId: " + productId + ", requested: " + requestedQty);
    }
}
