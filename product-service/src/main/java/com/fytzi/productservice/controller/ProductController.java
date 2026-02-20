package com.fytzi.productservice.controller;

import com.fytzi.productservice.dto.CreateProductRequest;
import com.fytzi.productservice.dto.OrderRequest;
import com.fytzi.productservice.dto.ProductListRequest;
import com.fytzi.productservice.dto.ProductResponseDto;
import com.fytzi.productservice.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Product Management", description = "Operations for listing, creating and managing products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @Operation(summary = "Add a new product", description = "Saves a new product entry to the catalog")
    public ResponseEntity<ProductResponseDto> create(@RequestBody CreateProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.createProduct(request));
    }

    @PostMapping("/validation")
    @Operation(summary = "Batch validate products", description = "Checks if multiple products exist and are active")
    public ResponseEntity<Boolean> getById(@RequestBody ProductListRequest prodList) {
        return ResponseEntity.ok(productService.getActiveProduct(prodList));
    }

    @GetMapping
    @Operation(summary = "List all active products", description = "Retrieves all products that are currently marked as active")
    public ResponseEntity<List<ProductResponseDto>> getAllProdcuts() {
        return ResponseEntity.ok(productService.getAllActiveProducts());
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "List products by category", description = "Retrieves all active products belonging to a specific category")
    public ResponseEntity<List<ProductResponseDto>> getByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(productService.getProductsByCategory(categoryId));
    }

    @PostMapping("/reduceStock")
    @Operation(summary = "Reserve product stock", description = "Checks and reduces quantity available for ordered items")
    public ResponseEntity<Boolean> reduceStock(@RequestBody OrderRequest orderRequest) {
        return ResponseEntity.ok(productService.reduceStock(orderRequest));
    }

    @GetMapping("/{productId}/exists")
    @Operation(summary = "Check product active status", description = "Quick check to see if a product is currently available")
    public ResponseEntity<Void> validateProductExists(@PathVariable Long productId) {
        if (!productService.getActiveProduct(productId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/details")
    @Operation(summary = "Get batch product details", description = "Retrieves full details for a list of product IDs")
    public ResponseEntity<List<ProductResponseDto>> getProductsDetails(@RequestBody ProductListRequest prodList) {
        return ResponseEntity.ok(productService.getProductsByIds(prodList));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update product", description = "Updates fields of an existing product")
    public ResponseEntity<ProductResponseDto> update(@PathVariable Long id, @RequestBody CreateProductRequest request) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product", description = "Marks a product as inactive (soft delete)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
