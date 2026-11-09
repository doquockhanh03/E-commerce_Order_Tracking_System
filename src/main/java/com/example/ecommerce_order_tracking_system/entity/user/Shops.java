package com.example.ecommerce_order_tracking_system.entity.user;

import com.example.ecommerce_order_tracking_system.common.enums.UserStatus;
import com.example.ecommerce_order_tracking_system.entity.order.Orders;
import com.example.ecommerce_order_tracking_system.entity.product.Products;
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
@Table(name = "shops")
public class Shops implements Serializable {

    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @ColumnDefault("'ACTIVE'")
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

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

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private Users owner;

    @OneToMany(mappedBy = "shop")
    private List<Products> products = new ArrayList<>();

    @NonNull
    @OneToMany(mappedBy = "shop")
    private List<Orders> orders = new ArrayList<>();
}
