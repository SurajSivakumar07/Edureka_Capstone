package com.fytzi.productservice.service.impl;

import com.fytzi.productservice.dto.CategoryResponseDto;
import com.fytzi.productservice.dto.CreateCategoryRequest;
import com.fytzi.productservice.entity.Category;
import com.fytzi.productservice.exception.CategoryNotFoundException;
import com.fytzi.productservice.repository.CategoryRepository;
import com.fytzi.productservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

        private final CategoryRepository categoryRepository;

        @Override
        public CategoryResponseDto create(CreateCategoryRequest request) {

                categoryRepository.findByName(request.getName())
                                .ifPresent(c -> {
                                        throw new RuntimeException("Category already exists: " + request.getName());
                                });

                Category category = Category.builder()
                                .name(request.getName())
                                .description(request.getDescription())
                                .createdAt(LocalDateTime.now())
                                .build();

                Category saved = categoryRepository.save(category);

                return new CategoryResponseDto(
                                saved.getId(),
                                saved.getName(),
                                saved.getDescription());
        }

        @Override
        public List<CategoryResponseDto> getAll() {
                return categoryRepository.findAll()
                                .stream()
                                .map(c -> new CategoryResponseDto(
                                                c.getId(),
                                                c.getName(),
                                                c.getDescription()))
                                .toList();
        }

        @Override
        public CategoryResponseDto getById(Long id) {
                Category c = categoryRepository.findById(id)
                                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
                return new CategoryResponseDto(c.getId(), c.getName(), c.getDescription());
        }

        @Override
        public CategoryResponseDto update(Long id, CreateCategoryRequest request) {
                Category category = categoryRepository.findById(id)
                                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));

                category.setName(request.getName());
                category.setDescription(request.getDescription());

                Category updated = categoryRepository.save(category);

                return new CategoryResponseDto(
                                updated.getId(),
                                updated.getName(),
                                updated.getDescription());
        }

        @Override
        public void delete(Long id) {
                if (!categoryRepository.existsById(id)) {
                        throw new CategoryNotFoundException("Category not found with id: " + id);
                }
                categoryRepository.deleteById(id);
        }
}
