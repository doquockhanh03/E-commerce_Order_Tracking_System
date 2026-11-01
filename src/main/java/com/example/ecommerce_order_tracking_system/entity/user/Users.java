package com.example.ecommerce_order_tracking_system.entity.user;

import com.example.ecommerce_order_tracking_system.common.enums.UserGender;
import com.example.ecommerce_order_tracking_system.common.enums.UserStatus;
import com.example.ecommerce_order_tracking_system.entity.order.Carts;
import com.example.ecommerce_order_tracking_system.entity.order.EmailNotifications;
import com.example.ecommerce_order_tracking_system.entity.order.Orders;
import com.example.ecommerce_order_tracking_system.entity.order.UserVouchers;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "users")
public class Users implements Serializable {

    @Id
    @Column(name = "id", nullable = false, length = 36)
    @UuidGenerator
    private String id;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private UserGender gender;

    @ColumnDefault("'ACTIVE'")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
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

    @NonNull
    @OneToMany(mappedBy = "user")
    private List<UserRoles> userRoles = new ArrayList<>();

    @NonNull
    @OneToMany(mappedBy = "user")
    private List<Addresses> addresses = new ArrayList<>();

    @OneToOne(mappedBy = "user")
    private Carts cart;

    @OneToOne(mappedBy = "owner")
    private Shops shop;

    @NonNull
    @OneToMany(mappedBy = "user")
    private List<UserVouchers> userVouchers = new ArrayList<>();

    @NonNull
    @OneToMany(mappedBy = "user")
    private List<Orders> orders = new ArrayList<>();

    @NonNull
    @OneToMany(mappedBy = "user")
    private List<EmailNotifications> emailNotifications = new ArrayList<>();
}
