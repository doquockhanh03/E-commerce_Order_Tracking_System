package com.example.ecommerce_order_tracking_system.repository.user;

import com.example.ecommerce_order_tracking_system.entity.user.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRolesRepo extends JpaRepository<UserRoles, String> {
}
