package com.fytzi.productservice.service.impl;
import com.fytzi.productservice.dto.*;
import com.fytzi.productservice.entity.*;
import com.fytzi.productservice.exception.CategoryNotFoundException;
import com.fytzi.productservice.repository.*;
import com.fytzi.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ProductResponseDto createProduct(CreateProductRequest request) {
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + request.categoryId()));

        Product product = Product.builder()
                .name(request.name())
                .description(request.description())
                .price(new BigDecimal(request.price()))
                .category(category)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();

        Product saved = productRepository.save(product);

        return mapToDto(saved);
    }

    @Override
    public ProductResponseDto getActiveProduct(Long id) {
        Product product = productRepository.findActiveById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return mapToDto(product);
    }

    @Override
    public List<ProductResponseDto> getAllActiveProducts() {
        return productRepository.findAllActive()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public List<ProductResponseDto> getProductsByCategory(Long categoryId) {
        return productRepository.findActiveByCategory(categoryId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    private ProductResponseDto mapToDto(Product p) {
        return new ProductResponseDto(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getPrice(),
                p.getCategory().getId(),
                p.getCategory().getName(),
                p.getIsActive()
        );
    }
}
