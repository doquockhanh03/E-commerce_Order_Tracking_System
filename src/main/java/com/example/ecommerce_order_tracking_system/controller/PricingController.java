package com.example.ecommerce_order_tracking_system.controller;

import com.example.ecommerce_order_tracking_system.dto.response.PricingSummaryResDto;
import com.example.ecommerce_order_tracking_system.security.CustomUserDetails;
import com.example.ecommerce_order_tracking_system.service.PricingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/pricing")
public class PricingController {

    private final PricingService orderService;

    @GetMapping("/summary")
    public ResponseEntity<PricingSummaryResDto> getOrderSummary (@AuthenticationPrincipal CustomUserDetails principal,
                                                                 @RequestParam List<String> cartItemIds,
                                                                 @RequestParam(required = false) String addressId,
                                                                 @RequestParam(required = false) String discountVoucherId,
                                                                 @RequestParam(required = false) String shippingVoucherId){
        return ResponseEntity.ok(orderService.getOrderSummary(principal.getUserId(), cartItemIds, addressId, discountVoucherId, shippingVoucherId));
    }
}
