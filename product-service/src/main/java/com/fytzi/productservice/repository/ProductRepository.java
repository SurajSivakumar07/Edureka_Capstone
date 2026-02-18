package com.fytzi.productservice.repository;

import com.fytzi.productservice.entity.Product;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT * FROM products WHERE id = :id AND is_active = true", nativeQuery = true)
    Optional<Product> findActiveById(@Param("id") Long id);

    @Query(value = "SELECT * FROM products WHERE category_id = :categoryId AND is_active = true", nativeQuery = true)
    List<Product> findActiveByCategory(@Param("categoryId") Long categoryId);

    @Query(value = "SELECT * FROM products WHERE is_active = true ORDER BY created_at DESC", nativeQuery = true)
    List<Product> findAllActive();
}
