package com.example.ecommerce_order_tracking_system.repository.user;

import com.example.ecommerce_order_tracking_system.entity.user.Addresses;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressesRepo extends JpaRepository<Addresses, String> {
    Optional<Addresses> findByIdAndUserId(String addressId, String userId);

    Optional<Addresses> findFirstByUserIdAndIsDefaultTrueAndDeletedFalse(String userId);
}
