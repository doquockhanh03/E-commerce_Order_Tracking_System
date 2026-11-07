package com.example.ecommerce_order_tracking_system.repository.order;

import com.example.ecommerce_order_tracking_system.entity.order.UserVouchers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserVouchersRepo extends JpaRepository<UserVouchers, String> {
    List<UserVouchers> findByUserId(String userId);

    Optional<UserVouchers> findByUserIdAndVoucherId(String userId, String voucherId);
}
