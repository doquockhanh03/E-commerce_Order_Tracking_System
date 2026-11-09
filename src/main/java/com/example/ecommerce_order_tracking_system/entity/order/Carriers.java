package com.example.ecommerce_order_tracking_system.entity.order;

import com.example.ecommerce_order_tracking_system.common.enums.ActiveStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.UuidGenerator;
import org.jspecify.annotations.NonNull;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "carriers")
public class Carriers implements Serializable {

    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "status")
    private ActiveStatus status;

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

    @NonNull
    @OneToMany(mappedBy = "carrier")
    private List<ShippingConfigs> shippingConfigs = new ArrayList<>();

    @NonNull
    @OneToMany(mappedBy = "carrier")
    private List<Orders> orders = new ArrayList<>();
}
