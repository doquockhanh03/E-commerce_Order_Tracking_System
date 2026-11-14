package com.example.ecommerce_order_tracking_system.service;

import com.example.ecommerce_order_tracking_system.dto.request.CreateOrderReqDto;
import com.example.ecommerce_order_tracking_system.dto.response.OrderResDto;

import java.util.List;

public interface OrdersService {
    public OrderResDto createOrder(String userId, List<String> cartItemIds, CreateOrderReqDto request);
}
