package com.fytzi.productservice.dto;

public record CreateProductRequest(
        String name,
        String description,
        Long quantity,
        Double price,
        Long categoryId
) {}
