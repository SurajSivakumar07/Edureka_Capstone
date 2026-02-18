package com.fytzi.productservice.repository;

import com.fytzi.productservice.entity.Category;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(value = "SELECT * FROM categories WHERE name = :name", nativeQuery = true)
    Optional<Category> findByName(@Param("name") String name);
}
