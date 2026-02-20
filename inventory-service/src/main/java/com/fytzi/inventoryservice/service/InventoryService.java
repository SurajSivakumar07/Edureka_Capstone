package com.fytzi.inventoryservice.service;

import java.util.List;

import com.fytzi.inventoryservice.dto.CreateOrUpdateInventoryRequest;
import com.fytzi.inventoryservice.dto.InventoryResponse;
import com.fytzi.inventoryservice.dto.ReleaseInventoryRequest;
import com.fytzi.inventoryservice.dto.ReserveInventoryRequest;

public interface InventoryService {

    InventoryResponse getStock(Long productId);

    void reserve(ReserveInventoryRequest request);

    void release(ReleaseInventoryRequest request);

    List<InventoryResponse> createOrUpdate(CreateOrUpdateInventoryRequest request);
}
