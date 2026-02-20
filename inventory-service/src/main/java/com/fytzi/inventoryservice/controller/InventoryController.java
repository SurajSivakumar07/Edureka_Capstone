package com.fytzi.inventoryservice.controller;

import com.fytzi.inventoryservice.dto.*;
import com.fytzi.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    // USER + ADMIN
    @GetMapping("/{productId}")
    public ResponseEntity<InventoryResponse> getStock(@PathVariable Long productId) {
        return ResponseEntity.ok(inventoryService.getStock(productId));
    }

    // Called by Order Service
    @PostMapping("/reserve")
    public ResponseEntity<Void> reserve(@RequestBody ReserveInventoryRequest request) {
        inventoryService.reserve(request);
        return ResponseEntity.ok().build();
    }

    // Called by Order Service (on cancel/failure)
    @PostMapping("/release")
    public ResponseEntity<Void> release(@RequestBody ReleaseInventoryRequest request) {
        inventoryService.release(request);
        return ResponseEntity.ok().build();
    }

    // ADMIN: create/update stock
    @PostMapping("/upsert")
    public ResponseEntity<java.util.List<InventoryResponse>> upsert(
            @RequestBody CreateOrUpdateInventoryRequest request) {
        return ResponseEntity.ok(inventoryService.createOrUpdate(request));
    }
}
