package com.fytzi.productservice.service;

import com.fytzi.productservice.dto.CreateProductRequest;
import com.fytzi.productservice.dto.ProductResponseDto;

import java.util.List;

public interface ProductService {

    ProductResponseDto createProduct(CreateProductRequest request);

    ProductResponseDto getActiveProduct(Long id);

    List<ProductResponseDto> getAllActiveProducts();

    List<ProductResponseDto> getProductsByCategory(Long categoryId);
}
