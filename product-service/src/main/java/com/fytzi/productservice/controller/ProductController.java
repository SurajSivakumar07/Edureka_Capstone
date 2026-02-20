package com.fytzi.productservice.controller;

import com.fytzi.productservice.dto.*;
import com.fytzi.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponseDto> create(@RequestBody CreateProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.createProduct(request));
    }

    @PostMapping("/validation")
    public ResponseEntity<Boolean> getById(@RequestBody ProductListRequest prodList) {
        return ResponseEntity.ok(productService.getActiveProduct(prodList));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAll() {
        return ResponseEntity.ok(productService.getAllActiveProducts());
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductResponseDto>> getByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(productService.getProductsByCategory(categoryId));
    }

    @PostMapping("/reduceStock")
    public ResponseEntity<Boolean> reduceStock(@RequestBody OrderRequest orderRequest) {
        return ResponseEntity.ok(productService.reduceStock(orderRequest));
    }

    @GetMapping("/{productId}/exists")
    public ResponseEntity<Void> validateProductExists(@PathVariable Long productId) {
        if (!productService.getActiveProduct(productId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/details")
    public ResponseEntity<List<ProductResponseDto>> getProductsDetails(@RequestBody ProductListRequest prodList) {
        return ResponseEntity.ok(productService.getProductsByIds(prodList));
    }
    }
}
