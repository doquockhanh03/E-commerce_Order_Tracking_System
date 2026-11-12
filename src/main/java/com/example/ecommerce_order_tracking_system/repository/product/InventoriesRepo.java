package com.example.ecommerce_order_tracking_system.repository.product;

import com.example.ecommerce_order_tracking_system.entity.product.Inventories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InventoriesRepo extends JpaRepository<Inventories, String> {

    @Modifying
    @Query("""
        Update Inventories a
            set a.reservedQuantity = a.reservedQuantity + :quantity
            where a.productVariant.id = :variantId
            and a.quantityInStock - a.reservedQuantity >= :quantity
    """)
    int reserve(@Param("variantId") String variantId, @Param("quantity") int quantity);
}