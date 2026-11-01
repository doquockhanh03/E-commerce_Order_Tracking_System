package com.example.ecommerce_order_tracking_system.repository.product;

import com.example.ecommerce_order_tracking_system.entity.product.Inventories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoriesRepo extends JpaRepository<Inventories, String> {
}
