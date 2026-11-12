package com.example.ecommerce_order_tracking_system.repository.order;

import com.example.ecommerce_order_tracking_system.entity.order.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemsRepo extends JpaRepository<CartItems, String> {
    public List<CartItems> getByProductVariantId(String id);

    public List<CartItems> getByCartId(String id);

    @Query("""
  SELECT DISTINCT c FROM CartItems c
  JOIN FETCH c.productVariant v
  JOIN FETCH v.product
  JOIN FETCH v.inventory
  LEFT JOIN FETCH v.productImages
  WHERE c.cart.id = :cartId AND c.deleted = false
  """)
    List<CartItems> getCartItemsWithDetails(@Param("cartId") String cartId);

    @Query("""
      SELECT c FROM CartItems c
      JOIN FETCH c.productVariant v
      JOIN FETCH v.product
      WHERE c.cart.id = :cartId AND c.id IN :cartItemIds AND c.deleted = false
      """)
    List<CartItems> getSelectedItems(@Param("cartId") String cartId, @Param("cartItemIds") List<String> cartItemIds);
}

