package com.fytzi.productservice.controller;

import com.fytzi.productservice.dto.*;
import com.fytzi.productservice.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "Category Management", description = "Managing product categories (e.g., Electronics, Fashion)")
public class CategoryController {

    private final CategoryService categoryService;

    // ADMIN only
    @PostMapping
    @Operation(summary = "Create a category", description = "Adds a new category for products (Admin only)")
    public ResponseEntity<CategoryResponseDto> create(@RequestBody CreateCategoryRequest request) {
        log.info("Data from paylaod is {}", request.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryService.create(request));
    }

    @GetMapping
    @Operation(summary = "List categories", description = "Returns all available product categories")
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAll());
    }

    // USER + ADMIN
    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID", description = "Finds a specific category using its unique ID")
    public ResponseEntity<CategoryResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update category", description = "Modifies an existing category's details")
    public ResponseEntity<CategoryResponseDto> update(@PathVariable Long id,
            @RequestBody CreateCategoryRequest request) {
        return ResponseEntity.ok(categoryService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete category", description = "Permanently removes a category from the system")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
