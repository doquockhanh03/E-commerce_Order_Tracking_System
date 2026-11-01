package com.example.ecommerce_order_tracking_system.repository.user;

import com.example.ecommerce_order_tracking_system.entity.user.Shops;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopsRepo extends JpaRepository<Shops, String> {
}
