package com.example.ecommerce_order_tracking_system.repository.order;

import com.example.ecommerce_order_tracking_system.entity.order.UserVouchers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserVouchersRepo extends JpaRepository<UserVouchers, String> {
    List<UserVouchers> findByUserId(String userId);

    Optional<UserVouchers> findByUserIdAndVoucherId(String userId, String voucherId);

    @Query("""
        SELECT DISTINCT uv FROM UserVouchers uv
            JOIN FETCH uv.voucher v
                WHERE uv.user.id = :userId and v.status = 'ACTIVE'
                    and v.usageLimit - v.usageCount > 0
                        and v.deleted = false
    """)
    List<UserVouchers> getUserVoucherWithDetail(@Param("userId") String userId);
}
