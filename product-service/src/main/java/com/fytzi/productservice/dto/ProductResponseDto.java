package com.fytzi.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponseDto {
        private Long id;
        private String name;
        private String description;
        private Long quantity;
        private BigDecimal price;
        private Long categoryId;
        private String categoryName;
        private Boolean isActive;
}
