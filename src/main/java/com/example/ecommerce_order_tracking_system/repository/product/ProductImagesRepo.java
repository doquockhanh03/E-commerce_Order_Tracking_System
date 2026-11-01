package com.example.ecommerce_order_tracking_system.repository.product;

import com.example.ecommerce_order_tracking_system.entity.product.ProductImages;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImagesRepo extends JpaRepository<ProductImages, String> {
}
