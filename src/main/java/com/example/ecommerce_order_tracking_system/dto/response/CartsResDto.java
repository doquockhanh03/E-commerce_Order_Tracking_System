package com.example.ecommerce_order_tracking_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartsResDto {
    private List<CartItemsResDto> itemsResDtoList;
}
