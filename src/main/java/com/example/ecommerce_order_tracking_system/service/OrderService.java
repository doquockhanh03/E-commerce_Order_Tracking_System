package com.example.ecommerce_order_tracking_system.service;

import com.example.ecommerce_order_tracking_system.dto.response.OrderSummaryResDto;

public interface OrderService {
    public OrderSummaryResDto getOrderSummary(String username, String discountVoucher, String shippingVoucher);
}
