package com.fytzi.productservice.repository;

import com.fytzi.productservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p.id FROM Product p WHERE p.id IN :ids AND p.isActive = true")
    List<Long> findExistingIds(@Param("ids") List<Long> ids);

    @Query(value = "SELECT * FROM products WHERE id IN (:ids)", nativeQuery = true)
    List<Product> findAllByIdNative(@Param("ids") List<Long> ids);

    @Query(value = "SELECT * FROM products WHERE category_id = :categoryId AND is_active = true", nativeQuery = true)
    List<Product> findActiveByCategory(@Param("categoryId") Long categoryId);

    @Query(value = "SELECT * FROM products WHERE is_active = true ORDER BY created_at DESC", nativeQuery = true)
    List<Product> findAllActive();

    @Modifying
    @Query(value = """
               UPDATE products
               SET quantity = quantity - :qty
               WHERE id = :productId AND  quantity >= :qty
            """, nativeQuery = true)
    int reserveStock(Long productId, int qty);
}
