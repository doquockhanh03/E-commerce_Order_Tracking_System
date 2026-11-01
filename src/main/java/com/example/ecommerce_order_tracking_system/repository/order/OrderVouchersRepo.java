package com.example.ecommerce_order_tracking_system.repository.order;

import com.example.ecommerce_order_tracking_system.entity.order.OrderVouchers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderVouchersRepo extends JpaRepository<OrderVouchers, String> {
}
