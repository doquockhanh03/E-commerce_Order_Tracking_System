package com.example.ecommerce_order_tracking_system.repository.order;

import com.example.ecommerce_order_tracking_system.entity.order.EmailNotifications;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailNotificationsRepo extends JpaRepository<EmailNotifications, String> {
}
