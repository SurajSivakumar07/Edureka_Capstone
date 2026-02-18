package com.fytzi.productservice.service;

import com.fytzi.productservice.dto.CategoryResponseDto;
import com.fytzi.productservice.dto.CreateCategoryRequest;

import java.util.List;

public interface CategoryService {

    CategoryResponseDto create(CreateCategoryRequest request);

    List<CategoryResponseDto> getAll();

    CategoryResponseDto getById(Long id);
}
