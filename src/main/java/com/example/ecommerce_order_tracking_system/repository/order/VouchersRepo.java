package com.example.ecommerce_order_tracking_system.repository.order;

import com.example.ecommerce_order_tracking_system.entity.order.Vouchers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VouchersRepo extends JpaRepository<Vouchers, String> {
}
