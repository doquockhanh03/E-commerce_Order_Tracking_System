package com.example.ecommerce_order_tracking_system.service;

import com.example.ecommerce_order_tracking_system.dto.response.ListUserDiscountResDto;

public interface DiscountService {
    public ListUserDiscountResDto getUserDiscount(String username);
}

