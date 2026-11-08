package com.example.ecommerce_order_tracking_system.dto.response;

import com.example.ecommerce_order_tracking_system.entity.order.OrderItems;
import com.example.ecommerce_order_tracking_system.entity.order.Payments;
import com.example.ecommerce_order_tracking_system.entity.order.TrackingLogs;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResDto {

    private List<OrderItemResDto> orderItems;

    private String address;

    private BigDecimal subTotal;

    private BigDecimal discountAmount;

    private BigDecimal shippingFee;

    private BigDecimal ShippingDiscountAmount;

    private BigDecimal totalAmount;

    private String trackingNumber;

    private TrackingLogResDto trackingLogs;

    private PaymentResDto payments;

    private String note;

    private Instant estimatedDelivery;
}
