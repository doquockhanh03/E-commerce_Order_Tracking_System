package com.example.ecommerce_order_tracking_system.service;

import com.example.ecommerce_order_tracking_system.dto.response.PricingSummaryResDto;

import java.util.List;

public interface PricingService {
    public PricingSummaryResDto getOrderSummary(String userId, List<String> cartItemIds, String addressId, String discountVoucher, String shippingVoucher);
}
