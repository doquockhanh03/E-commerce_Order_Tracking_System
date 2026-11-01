package com.example.ecommerce_order_tracking_system.repository.order;

import com.example.ecommerce_order_tracking_system.entity.order.UserVouchers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserVouchersRepo extends JpaRepository<UserVouchers, String> {
}
