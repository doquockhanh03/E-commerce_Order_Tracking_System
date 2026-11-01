package com.example.ecommerce_order_tracking_system.repository.product;

import com.example.ecommerce_order_tracking_system.entity.product.ProductVariants;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductVariantsRepo extends JpaRepository<ProductVariants, String> {
}
