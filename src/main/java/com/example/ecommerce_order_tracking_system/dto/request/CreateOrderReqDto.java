package com.example.ecommerce_order_tracking_system.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateOrderReqDto {

    private String discountVoucherId;

    private String shippingVoucherId;

    private String carrierId;

    private String addressId;

    private String note;

    private String paymentMethodId;
}
