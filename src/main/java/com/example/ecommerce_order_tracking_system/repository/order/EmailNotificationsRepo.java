package com.example.ecommerce_order_tracking_system.repository.order;

import com.example.ecommerce_order_tracking_system.common.enums.EmailStatus;
import com.example.ecommerce_order_tracking_system.entity.order.EmailNotifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmailNotificationsRepo extends JpaRepository<EmailNotifications, String> {

    @Query("""
      SELECT e FROM EmailNotifications e
      JOIN FETCH e.user
      JOIN FETCH e.order
      WHERE e.status = :status
  """)
    List<EmailNotifications> findByStatusWithDetails(@Param("status") EmailStatus status);
}
