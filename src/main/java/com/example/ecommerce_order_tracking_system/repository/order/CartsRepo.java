package com.example.ecommerce_order_tracking_system.repository.order;

import com.example.ecommerce_order_tracking_system.entity.order.Carts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartsRepo extends JpaRepository<Carts, String> {
    Optional<Carts> getByUserId(String userId);

    //    @Query("select E.name, D.name, D.price, D.sku, c.quantity, (g.quantityInStock - g.reservedQuantity) as QuantityInStock, F.imageUrl FROM  Carts B " +
//            "inner join CartItems C on B.id = C.cart " +
//            "inner join ProductVariants D on C.productVariant = D.id " +
//            "inner join Products E on D.product = E.id " +
//            "inner join ProductImages F on D.id = F.productVariant " +
//            "inner join Inventories G on D.id = G.productVariant " +
//            "where B.id = :userId and f.displayOrder = 1")
//    List<CartItemsResDto> getItemsInCart(@Param("userId") String userId);
}
