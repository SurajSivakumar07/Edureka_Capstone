package com.fytzi.inventoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryResponse {
        private Long productId;
        private Integer availableQty;
        private Integer reservedQty;
        private Boolean inStock;
}
