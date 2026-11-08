package com.example.ecommerce_order_tracking_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResDto {

    private String paymentGateway;

    private Instant paid_at;
}
