package com.fytzi.productservice.service.impl;

import com.fytzi.productservice.dto.*;
import com.fytzi.productservice.entity.*;
import com.fytzi.productservice.exception.CategoryNotFoundException;
import com.fytzi.productservice.exception.InsufficientStock;
import com.fytzi.productservice.exception.ProductException;
import com.fytzi.productservice.repository.*;
import com.fytzi.productservice.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ProductResponseDto createProduct(CreateProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(
                        () -> new CategoryNotFoundException("Category not found with id: " + request.getCategoryId()));

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .quantity(request.getQuantity())
                .price(BigDecimal.valueOf(request.getPrice()))
                .category(category)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();

        Product saved = productRepository.save(product);

        return mapToDto(saved);
    }

    @Override
    public Boolean getActiveProduct(Long id) {
        return productRepository.existsById(id);
    }

    @Override
    public Boolean getActiveProduct(ProductListRequest productList) {
        List<Long> exsistingProducts = productRepository.findExistingIds(productList.getProductId());
        Set<Long> existingSet = new HashSet<>(exsistingProducts);
        List<Long> missingList = productList.getProductId().stream()
                .filter(id -> !existingSet.contains(id))
                .toList();
        if (!missingList.isEmpty()) {
            throw new ProductException("These are invalid product ids " + missingList.toString());
        }

        return true;
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
                p.getQuantity(),
                p.getPrice(),
                p.getCategory() != null ? p.getCategory().getId() : null,
                p.getCategory() != null ? p.getCategory().getName() : null,
                p.getIsActive());
    }

    @Override
    @Transactional
    public Boolean reduceStock(OrderRequest orderRequest) {
        for (int i = 0; i < orderRequest.getItems().size(); i++) {
            int updated = productRepository.reserveStock(
                    orderRequest.getItems().get(i).getProductId(),
                    orderRequest.getItems().get(i).getQuantity());
            if (updated == 0) {
                throw new InsufficientStock("Not enough stock for " + orderRequest.getItems().get(i).getProductId());
            }
        }
        return true;
    }

    @Override
    @Transactional
    public List<ProductResponseDto> getProductsByIds(ProductListRequest prodList) {
        return productRepository.findAllById(prodList.getProductId())
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public ProductResponseDto updateProduct(Long id, CreateProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductException("Product not found with id: " + id));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(
                        () -> new CategoryNotFoundException("Category not found with id: " + request.getCategoryId()));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setQuantity(request.getQuantity());
        product.setPrice(BigDecimal.valueOf(request.getPrice()));
        product.setCategory(category);

        Product updated = productRepository.save(product);
        return mapToDto(updated);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductException("Product not found with id: " + id));

        // Soft delete based on isActive field
        product.setIsActive(false);
        productRepository.save(product);
    }
}
