package com.example.ecommerce_order_tracking_system.repository.order;

import com.example.ecommerce_order_tracking_system.entity.order.Carts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartsRepo extends JpaRepository<Carts, String> {
}
