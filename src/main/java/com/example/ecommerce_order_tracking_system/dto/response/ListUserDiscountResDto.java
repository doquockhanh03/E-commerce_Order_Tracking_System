package com.example.ecommerce_order_tracking_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ListUserDiscountResDto {
    private List<UserDiscountResDto> userDiscountResDtoList;
}
