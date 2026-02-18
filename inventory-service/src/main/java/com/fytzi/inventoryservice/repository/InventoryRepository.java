package com.fytzi.inventoryservice.repository;

import com.fytzi.inventoryservice.entity.Inventory;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    @Query(value = "SELECT * FROM inventory WHERE product_id = :productId", nativeQuery = true)
    Optional<Inventory> findByProductId(@Param("productId") Long productId);

    @Modifying
    @Query(value = """
        UPDATE inventory 
        SET available_qty = available_qty - :qty,
            reserved_qty = reserved_qty + :qty,
            updated_at = NOW()
        WHERE product_id = :productId
          AND available_qty >= :qty
        """, nativeQuery = true)
    int reserveStock(@Param("productId") Long productId, @Param("qty") int qty);

    @Modifying
    @Query(value = """
        UPDATE inventory 
        SET available_qty = available_qty + :qty,
            reserved_qty = reserved_qty - :qty,
            updated_at = NOW()
        WHERE product_id = :productId
          AND reserved_qty >= :qty
        """, nativeQuery = true)
    int releaseStock(@Param("productId") Long productId, @Param("qty") int qty);
}
