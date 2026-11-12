package com.example.ecommerce_order_tracking_system.service;

import com.example.ecommerce_order_tracking_system.dto.request.AddToCartReqDto;
import com.example.ecommerce_order_tracking_system.dto.response.CartsResDto;

public interface CartsService {
        public void addToCart(AddToCartReqDto request);

        public CartsResDto getCartDetail (String userId);

        public CartsResDto updateCartItemQuantity(String userId, String cartItemId, String action);
}
