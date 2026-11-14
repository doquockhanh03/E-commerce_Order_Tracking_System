package com.example.ecommerce_order_tracking_system.repository.product;

import com.example.ecommerce_order_tracking_system.entity.product.Inventories;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InventoriesRepo extends JpaRepository<Inventories, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("Select i from Inventories i where i.productVariant.id = :variantId")
    Optional<Inventories> findByProductVariantIdForUpdate(@Param("variantId") String variantId);
}