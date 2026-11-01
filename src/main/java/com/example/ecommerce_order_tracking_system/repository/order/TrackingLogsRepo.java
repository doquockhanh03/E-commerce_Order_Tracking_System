package com.example.ecommerce_order_tracking_system.repository.order;

import com.example.ecommerce_order_tracking_system.entity.order.TrackingLogs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackingLogsRepo extends JpaRepository<TrackingLogs, String> {
}
