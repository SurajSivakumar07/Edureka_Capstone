package com.fytzi.inventoryservice.service;

import java.util.List;

import com.fytzi.inventoryservice.dto.*;

public interface InventoryService {

    InventoryResponse getStock(Long productId);

    void reserve(ReserveInventoryRequest request);

    void release(ReleaseInventoryRequest request);

    List<InventoryResponse> createOrUpdate(CreateOrUpdateInventoryRequest request);
}
