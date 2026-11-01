package com.example.ecommerce_order_tracking_system.repository.user;

import com.example.ecommerce_order_tracking_system.entity.user.Addresses;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressesRepo extends JpaRepository<Addresses, String> {
}
