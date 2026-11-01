package com.example.ecommerce_order_tracking_system.repository.order;

import com.example.ecommerce_order_tracking_system.entity.order.ShippingConfigs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingConfigsRepo extends JpaRepository<ShippingConfigs, String> {
}
