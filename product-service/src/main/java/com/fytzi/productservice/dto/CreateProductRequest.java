package com.fytzi.productservice.dto;

public record CreateProductRequest(
        String name,
        String description,
        String price,
        Long categoryId
) {}
