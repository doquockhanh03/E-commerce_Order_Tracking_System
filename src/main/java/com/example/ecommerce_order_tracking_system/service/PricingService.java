package com.example.ecommerce_order_tracking_system.service;

import com.example.ecommerce_order_tracking_system.dto.response.PricingSummaryResDto;

public interface PricingService {
    public PricingSummaryResDto getOrderSummary(String username, String discountVoucher, String shippingVoucher);
}
