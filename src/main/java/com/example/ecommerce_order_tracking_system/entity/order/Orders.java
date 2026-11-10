package com.example.ecommerce_order_tracking_system.entity.order;

import com.example.ecommerce_order_tracking_system.common.enums.OrderStatus;
import com.example.ecommerce_order_tracking_system.common.enums.OrderPaymentStatus;
import com.example.ecommerce_order_tracking_system.entity.user.Shops;
import com.example.ecommerce_order_tracking_system.entity.user.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.UuidGenerator;
import org.jspecify.annotations.NonNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "orders")
public class Orders implements Serializable {

    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @Column(name = "recipient_name", nullable = false)
    private String recipientName;

    @Column(name = "recipient_phone", nullable = false)
    private String recipientPhone;

    @Column(name = "shipping_address", nullable = false, length = 500)
    private String shippingAddress;

    @Column(name = "note")
    private String note;

    @Column(name = "subtotal", precision = 15, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "discount_amount", precision = 15, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "shipping_fee", precision = 15, scale = 2)
    private BigDecimal shippingFee;

    @Column(name = "shipping_discount_amount", precision = 15, scale = 2)
    private BigDecimal shippingDiscountAmount;

    @Column(name = "total_amount", precision = 15, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "estimated_delivery_at")
    private Instant estimatedDeliveryAt;

    @Column(name = "delivered_at")
    private Instant deliveredAt;

    @ColumnDefault("'PENDING'")
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ColumnDefault("'AWAITING_PAYMENT'")
    @Column(name = "payment_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderPaymentStatus paymentStatus;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "created_by", length = 36)
    private String createdBy;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "updated_by", length = 36)
    private String updatedBy;

    @ColumnDefault("0")
    @Column(name = "deleted")
    private Boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shops shop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrier_id")
    private Carriers carrier;

    @NonNull
    @OneToMany(mappedBy = "order")
    private List<Payments> payments = new ArrayList<>();

    @NonNull
    @OneToMany(mappedBy = "order")
    private List<OrderVouchers> orderVouchers = new ArrayList<>();

    @NonNull
    @OneToMany(mappedBy = "order")
    private List<OrderItems> orderItems = new ArrayList<>();

    @NonNull
    @OneToMany(mappedBy = "order")
    private List<EmailNotifications> emailNotifications = new ArrayList<>();

    @NonNull
    @OneToMany(mappedBy = "order")
    private List<TrackingLogs> trackingLogs = new ArrayList<>();

    @NonNull
    @OneToMany(mappedBy = "order")
    private List<ReturnRequests> returnRequests = new ArrayList<>();

    @NonNull
    @OneToMany(mappedBy = "order")
    private List<Refunds> refunds = new ArrayList<>();
}
