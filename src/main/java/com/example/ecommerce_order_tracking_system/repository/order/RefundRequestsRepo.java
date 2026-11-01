package com.example.ecommerce_order_tracking_system.repository.order;

import com.example.ecommerce_order_tracking_system.entity.order.ReturnRequests;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundRequestsRepo extends JpaRepository<ReturnRequests, String> {
}
