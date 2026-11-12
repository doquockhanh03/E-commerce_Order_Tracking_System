package com.example.ecommerce_order_tracking_system.controller;

import com.example.ecommerce_order_tracking_system.dto.response.ListUserDiscountResDto;
import com.example.ecommerce_order_tracking_system.security.CustomUserDetails;
import com.example.ecommerce_order_tracking_system.service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/discounts")
public class DiscountController {

    private final DiscountService discountService;

    @GetMapping("/available")
    public ResponseEntity<ListUserDiscountResDto> getUserDiscounts(@AuthenticationPrincipal CustomUserDetails principal) {

        return ResponseEntity.ok(discountService.getUserDiscount(principal.getUserId()));
    }
}
