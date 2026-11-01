package com.example.ecommerce_order_tracking_system.repository.product;

import com.example.ecommerce_order_tracking_system.entity.product.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriesRepo extends JpaRepository<Categories, String> {
}
