package com.example.ecommerce_order_tracking_system.controller;

import com.example.ecommerce_order_tracking_system.dto.request.CreateOrderReqDto;
import com.example.ecommerce_order_tracking_system.dto.response.OrderResDto;
import com.example.ecommerce_order_tracking_system.security.CustomUserDetails;
import com.example.ecommerce_order_tracking_system.service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrdersService ordersService;

    @PostMapping
    public ResponseEntity<OrderResDto> createOrder (@AuthenticationPrincipal CustomUserDetails principal,
                                                    @RequestParam List<String> cartItemIds,
                                                    @RequestBody CreateOrderReqDto request){

        return ResponseEntity.ok(ordersService.createOrder(principal.getUserId(), cartItemIds, request));
    }
}
