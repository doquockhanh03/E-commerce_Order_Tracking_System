package com.example.ecommerce_order_tracking_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PricingSummaryResDto {
    private BigDecimal subTotal;

    private BigDecimal bulkDiscount;

    private BigDecimal expeditedShipping;

    private BigDecimal shippingDiscount;

    private BigDecimal totalAmount;
}
