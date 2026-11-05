package com.example.ecommerce_order_tracking_system.service;

import com.example.ecommerce_order_tracking_system.dto.request.RegisterUserDto;
import com.example.ecommerce_order_tracking_system.dto.response.UserResponseDto;

public interface AuthService {
    public UserResponseDto register(RegisterUserDto request);

    public UserResponseDto login(String username);
}
