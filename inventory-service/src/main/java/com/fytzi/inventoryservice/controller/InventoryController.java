package com.fytzi.inventoryservice.controller;

import com.fytzi.inventoryservice.dto.*;
import com.fytzi.inventoryservice.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
@Tag(name = "Inventory Management", description = "Operations for tracking and managing product stock levels")
public class InventoryController {

    private final InventoryService inventoryService;

    // USER + ADMIN
    @GetMapping("/{productId}")
    @Operation(summary = "Get product stock", description = "Retrieves the current stock level for a specific product")
    public ResponseEntity<InventoryResponse> getStock(@PathVariable Long productId) {
        return ResponseEntity.ok(inventoryService.getStock(productId));
    }

    // Called by Order Service
    @PostMapping("/reserve")
    @Operation(summary = "Reserve stock", description = "Locks a certain amount of stock during the order creation process")
    public ResponseEntity<Void> reserve(@RequestBody ReserveInventoryRequest request) {
        inventoryService.reserve(request);
        return ResponseEntity.ok().build();
    }

    // Called by Order Service (on cancel/failure)
    @PostMapping("/release")
    @Operation(summary = "Release reserved stock", description = "Returns reserved stock back to available inventory on order cancellation")
    public ResponseEntity<Void> release(@RequestBody ReleaseInventoryRequest request) {
        inventoryService.release(request);
        return ResponseEntity.ok().build();
    }

    // ADMIN: create/update stock
    @PostMapping("/upsert")
    @Operation(summary = "Update or create stock", description = "Administrative endpoint to update or create stock entries for products")
    public ResponseEntity<java.util.List<InventoryResponse>> upsert(
            @RequestBody CreateOrUpdateInventoryRequest request) {
        return ResponseEntity.ok(inventoryService.createOrUpdate(request));
    }
}
