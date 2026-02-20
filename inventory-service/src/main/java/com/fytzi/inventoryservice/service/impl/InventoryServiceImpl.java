package com.fytzi.inventoryservice.service.impl;

import com.fytzi.inventoryservice.clinet.ProductClient;
import com.fytzi.inventoryservice.dto.*;
import com.fytzi.inventoryservice.entity.Inventory;
import com.fytzi.inventoryservice.exception.*;
import com.fytzi.inventoryservice.repository.InventoryRepository;
import com.fytzi.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductClient productClient;

    @Override
    public InventoryResponse getStock(Long productId) {
        Inventory inv = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new InventoryNotFoundException(productId));

        return new InventoryResponse(
                inv.getProductId(),
                inv.getAvailableQty(),
                inv.getReservedQty(),
                inv.getAvailableQty() > 0);
    }

    @Override
    @Transactional
    public void reserve(ReserveInventoryRequest request) {
        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new InvalidInventoryOperationException("Quantity must be greater than 0");
        }

        int updated = inventoryRepository.reserveStock(
                request.getProductId(),
                request.getQuantity());

        if (updated == 0) {
            throw new InsufficientStockException(request.getProductId(), request.getQuantity());
        }
    }

    @Override
    @Transactional
    public void release(ReleaseInventoryRequest request) {
        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new InvalidInventoryOperationException("Quantity must be greater than 0");
        }

        int updated = inventoryRepository.releaseStock(
                request.getProductId(),
                request.getQuantity());

        if (updated == 0) {
            throw new InvalidInventoryOperationException(
                    "No reserved stock to release for productId: " + request.getProductId());
        }
    }

    @Override
    @Transactional
    public java.util.List<InventoryResponse> createOrUpdate(CreateOrUpdateInventoryRequest request) {
        return request.getInventories().stream().map(item -> {
            if (item.getProductId() == null) {
                throw new InvalidInventoryOperationException("Product ID cannot be null");
            }
            if (item.getAvailableQty() == null || item.getAvailableQty() < 0) {
                throw new InvalidQuantityException("Available qty must be >= 0 for productId=" + item.getProductId());
            }

            try {
                productClient.validateProductExists(item.getProductId());
            } catch (Exception ex) {
                throw new InvalidInventoryOperationException(
                        "Cannot create inventory. Product does not exist: " + item.getProductId());
            }

            Inventory inv = inventoryRepository.findByProductId(item.getProductId())
                    .orElse(Inventory.builder()
                            .productId(item.getProductId())
                            .availableQty(0)
                            .reservedQty(0)
                            .build());

            inv.setAvailableQty(item.getAvailableQty());
            inv.setUpdatedAt(LocalDateTime.now());

            Inventory saved = inventoryRepository.save(inv);

            return new InventoryResponse(
                    saved.getProductId(),
                    saved.getAvailableQty(),
                    saved.getReservedQty(),
                    saved.getAvailableQty() > 0);
        }).collect(java.util.stream.Collectors.toList());
    }
}
