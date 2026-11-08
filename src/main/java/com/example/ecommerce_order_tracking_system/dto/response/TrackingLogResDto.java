package com.example.ecommerce_order_tracking_system.dto.response;


import com.example.ecommerce_order_tracking_system.common.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TrackingLogResDto {

    private String description;

    private OrderStatus status;
}
