package com.example.ecommerce_order_tracking_system.repository.user;

import com.example.ecommerce_order_tracking_system.entity.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepo extends JpaRepository<Users, String> {
    Boolean existsByEmail(String email);

    Boolean existsByPhoneNumber(String phoneNumber);
}
