package com.example.ecommerce_order_tracking_system.repository.order;

import com.example.ecommerce_order_tracking_system.entity.order.Carriers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarriersRepo extends JpaRepository<Carriers, String> {
}
