package com.fytzi.orderservice.client;

import com.fytzi.orderservice.dto.InventoryReserveRequest;
import com.fytzi.orderservice.dto.InventoryReserveResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "INVENTORY-SERVICE")
public interface InventoryClient {

    @PostMapping("/inventory/reserve")
    InventoryReserveResponse reserve(@RequestBody InventoryReserveRequest request);
}
