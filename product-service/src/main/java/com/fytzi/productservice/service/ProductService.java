package com.fytzi.productservice.service;

import com.fytzi.productservice.dto.CreateProductRequest;
import com.fytzi.productservice.dto.OrderRequest;
import com.fytzi.productservice.dto.ProductListRequest;
import com.fytzi.productservice.dto.ProductResponseDto;

import java.util.List;

public interface ProductService {

    ProductResponseDto createProduct(CreateProductRequest request);

    Boolean getActiveProduct(Long id);

    Boolean getActiveProduct(ProductListRequest prodList);

    List<ProductResponseDto> getAllActiveProducts();

    List<ProductResponseDto> getProductsByCategory(Long categoryId);

    Boolean reduceStock(OrderRequest orderRequest);

    List<ProductResponseDto> getProductsByIds(ProductListRequest prodList);
}
