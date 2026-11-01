package com.example.ecommerce_order_tracking_system.entity.order;

import com.example.ecommerce_order_tracking_system.common.enums.VoucherStatus;
import com.example.ecommerce_order_tracking_system.common.enums.VoucherType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
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
@Table(name = "vouchers")
public class Vouchers implements Serializable {

    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "voucher_code", nullable = false)
    private String voucherCode;

    @Column(name = "voucher_type")
    @Enumerated(EnumType.STRING)
    private VoucherType voucherType;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private VoucherStatus status;

    @Column(name = "description")
    private String description;

    @Column(name = "discount_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal discountValue;

    @Column(name = "min_order_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal minOrderAmount;

    @Column(name = "usage_limit", nullable = false)
    private Integer usageLimit;

    @Column(name = "usage_count", nullable = false)
    private Integer usageCount;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "ended_at")
    private Instant endedAt;

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

    @OneToMany(mappedBy = "voucher")
    private List<UserVouchers> userVouchers = new ArrayList<>();

    @NonNull
    @OneToMany(mappedBy = "voucher")
    private List<OrderVouchers> orderVouchers = new ArrayList<>();
}
