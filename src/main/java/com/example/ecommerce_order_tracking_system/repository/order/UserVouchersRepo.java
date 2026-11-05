package com.example.ecommerce_order_tracking_system.repository.order;

import com.example.ecommerce_order_tracking_system.entity.order.UserVouchers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserVouchersRepo extends JpaRepository<UserVouchers, String> {
    public List<UserVouchers> findByUserId(String userId);
}
