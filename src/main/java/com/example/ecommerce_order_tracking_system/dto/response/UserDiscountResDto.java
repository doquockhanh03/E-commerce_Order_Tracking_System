package com.example.ecommerce_order_tracking_system.dto.response;

import com.example.ecommerce_order_tracking_system.common.enums.VoucherStatus;
import com.example.ecommerce_order_tracking_system.common.enums.VoucherType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDiscountResDto {
    String voucherCode;

    VoucherType voucherType;

    String description;

    BigDecimal discountValue;

    BigDecimal minOrderAmount;

    VoucherStatus voucherStatus;
}
