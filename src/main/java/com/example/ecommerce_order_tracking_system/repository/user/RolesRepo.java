package com.example.ecommerce_order_tracking_system.repository.user;

import com.example.ecommerce_order_tracking_system.entity.user.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepo extends JpaRepository<Roles, String> {
}
