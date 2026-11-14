package com.example.ecommerce_order_tracking_system.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartItemActionReqDto {
    private String action;

    private Integer quantity;
}
