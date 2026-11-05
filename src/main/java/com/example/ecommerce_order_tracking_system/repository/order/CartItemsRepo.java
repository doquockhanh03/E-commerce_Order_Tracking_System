package com.example.ecommerce_order_tracking_system.repository.order;

import com.example.ecommerce_order_tracking_system.entity.order.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemsRepo extends JpaRepository<CartItems, String> {
    public List<CartItems> getByProductVariantId(String id);

    public List<CartItems> getByCartId(String id);
}
