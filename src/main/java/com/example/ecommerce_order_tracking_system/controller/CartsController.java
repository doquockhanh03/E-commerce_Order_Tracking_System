package com.example.ecommerce_order_tracking_system.controller;

import com.example.ecommerce_order_tracking_system.dto.request.CartItemActionReqDto;
import com.example.ecommerce_order_tracking_system.dto.response.CartsResDto;
import com.example.ecommerce_order_tracking_system.service.CartsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/carts")
public class CartsController {

    private final CartsService cartsService;

    @GetMapping("/detail")
    public ResponseEntity<CartsResDto> getCartDetail (Authentication authentication){
        String username = authentication.getName();

        return ResponseEntity.ok(cartsService.getCartDetail(username));
    }

    @PatchMapping("/item/quantity/{cartItemId}")
    public ResponseEntity<CartsResDto> updateCartItemQuantity (Authentication authentication,
                                                               @PathVariable String cartItemId,
                                                               @RequestBody CartItemActionReqDto action){
        String username = authentication.getName();
        return ResponseEntity.ok(cartsService.updateCartItemQuantity(username, cartItemId, action.getAction()));

    }
}