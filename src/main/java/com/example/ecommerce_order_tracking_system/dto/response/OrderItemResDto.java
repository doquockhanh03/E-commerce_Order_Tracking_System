package com.example.ecommerce_order_tracking_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResDto {
    private String productName;

    private String variant_name;

    private String sku;

    private String quantity;

    private String unitPrice;
}
