package com.example.ecommerce_order_tracking_system.repository.product;

import com.example.ecommerce_order_tracking_system.entity.product.Products;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRepo extends JpaRepository<Products, String> {
}
