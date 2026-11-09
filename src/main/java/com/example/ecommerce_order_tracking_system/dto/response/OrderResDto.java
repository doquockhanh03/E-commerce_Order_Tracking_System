package com.example.ecommerce_order_tracking_system.dto.response;

import com.example.ecommerce_order_tracking_system.entity.order.OrderItems;
import com.example.ecommerce_order_tracking_system.entity.order.Payments;
import com.example.ecommerce_order_tracking_system.entity.order.TrackingLogs;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.query.Order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResDto {

    private List<String> orderIds;

    private BigDecimal totalAmount;

}
