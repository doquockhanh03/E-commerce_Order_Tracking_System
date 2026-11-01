package com.example.ecommerce_order_tracking_system.entity.order;

import com.example.ecommerce_order_tracking_system.common.enums.RequestStatus;
import com.example.ecommerce_order_tracking_system.common.enums.ReturnRequestType;
import com.example.ecommerce_order_tracking_system.common.enums.ReturnShippingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;
import java.time.Instant;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "return_requests")
public class ReturnRequests implements Serializable {

    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "reason")
    private String reason;

    @Column(name = "origin_type")
    private ReturnRequestType originType;

    @Column(name = "location_name", nullable = false)
    private String locationName;

    @Column(name = "return_shipping_status")
    private ReturnShippingStatus returnShippingStatus;

    @Lob
    @Column(name = "request_status")
    private RequestStatus requestStatus;

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
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;

    @OneToOne(mappedBy = "returnRequest")
    private Refunds refund;
}
