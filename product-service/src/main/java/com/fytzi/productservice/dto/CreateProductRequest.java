package com.fytzi.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateProductRequest {
        private String name;
        private String description;
        private Long quantity;
        private Double price;
        private Long categoryId;
}
