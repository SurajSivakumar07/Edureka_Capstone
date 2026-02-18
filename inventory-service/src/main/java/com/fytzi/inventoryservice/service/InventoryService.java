package com.fytzi.inventoryservice.service;

import com.fytzi.inventoryservice.dto.*;

public interface InventoryService {

    InventoryResponse getStock(Long productId);

    void reserve(ReserveInventoryRequest request);

    void release(ReleaseInventoryRequest request);

    InventoryResponse createOrUpdate(CreateOrUpdateInventoryRequest request);
}
