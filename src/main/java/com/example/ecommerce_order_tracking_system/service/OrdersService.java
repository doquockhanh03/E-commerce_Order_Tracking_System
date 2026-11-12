package com.example.ecommerce_order_tracking_system.service;

import com.example.ecommerce_order_tracking_system.dto.request.CreateOrderReqDto;
import com.example.ecommerce_order_tracking_system.dto.response.OrderResDto;

public interface OrdersService {
    public OrderResDto createOrder(String userId, CreateOrderReqDto request);
}
