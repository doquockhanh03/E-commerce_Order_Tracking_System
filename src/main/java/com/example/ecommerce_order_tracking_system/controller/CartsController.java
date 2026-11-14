package com.example.ecommerce_order_tracking_system.controller;

import com.example.ecommerce_order_tracking_system.dto.request.CartItemActionReqDto;
import com.example.ecommerce_order_tracking_system.dto.response.CartsResDto;
import com.example.ecommerce_order_tracking_system.security.CustomUserDetails;
import com.example.ecommerce_order_tracking_system.service.CartsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/carts")
public class CartsController {

    private final CartsService cartsService;

    @GetMapping("/detail")
    public ResponseEntity<CartsResDto> getCartDetail (@AuthenticationPrincipal CustomUserDetails principal){

        return ResponseEntity.ok(cartsService.getCartDetail(principal.getUserId()));
    }

    @PatchMapping("/item/quantity/{cartItemId}")
    public ResponseEntity<CartsResDto> updateCartItemQuantity (@AuthenticationPrincipal CustomUserDetails principal,
                                                               @PathVariable String cartItemId,
                                                               @RequestBody CartItemActionReqDto request){
        return ResponseEntity.ok(cartsService.updateCartItemQuantity(principal.getUserId(), cartItemId, request));

    }
}