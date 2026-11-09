package com.example.ecommerce_order_tracking_system.entity.product;

import com.example.ecommerce_order_tracking_system.entity.order.CartItems;
import com.example.ecommerce_order_tracking_system.entity.order.OrderItems;
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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "product_variants")
public class ProductVariants implements Serializable {

    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false, precision = 15, scale = 2)
    private BigDecimal price;

    @Column(name = "sku", nullable = false)
    private String sku;

    @Column(name = "color")
    private String color;

    @Column(name = "size")
    private String size;

    @Column(name = "weight", precision = 10, scale = 2)
    private BigDecimal weight;

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
    @JoinColumn(name = "product_id", nullable = false)
    private Products product;

    @NonNull
    @OneToMany(mappedBy = "productVariant")
    private List<ProductImages> productImages = new ArrayList<>();

    @OneToOne(mappedBy = "productVariant")
    private Inventories inventory;

    @NonNull
    @OneToMany(mappedBy = "productVariant")
    private List<CartItems> cartItems = new ArrayList<>();

    @NonNull
    @OneToMany(mappedBy = "productVariant")
    private List<OrderItems> orderItems = new ArrayList<>();
}
