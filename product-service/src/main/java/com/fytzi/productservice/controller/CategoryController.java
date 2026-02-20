package com.fytzi.productservice.controller;

import com.fytzi.productservice.dto.*;
import com.fytzi.productservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // ADMIN only
    @PostMapping
    public ResponseEntity<CategoryResponseDto> create(@RequestBody CreateCategoryRequest request) {
        log.info("Data from paylaod is {}",request.name());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAll() {
        return ResponseEntity.ok(categoryService.getAll());
    }

    // USER + ADMIN
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getById(id));
    }
}
