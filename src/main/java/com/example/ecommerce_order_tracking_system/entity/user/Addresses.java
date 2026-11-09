package com.example.ecommerce_order_tracking_system.entity.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.time.Instant;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "addresses")
public class Addresses implements Serializable {

    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "recipient_name", nullable = false)
    private String recipientName;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "hamlet_village", nullable = false)
    private String hamletVillage;

    @Column(name = "ward_commune", nullable = false)
    private String wardCommune;

    @Column(name = "province_city", nullable = false, length = 100)
    private String provinceCity;

    @Column(name = "address_detail", nullable = false)
    private String addressDetail;

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

    @NotNull
    @ColumnDefault("0")
    @Column(name = "is_default", nullable = false)
    private Boolean isDefault;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;
}
