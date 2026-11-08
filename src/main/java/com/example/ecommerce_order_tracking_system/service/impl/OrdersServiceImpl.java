package com.example.ecommerce_order_tracking_system.service.impl;

import com.example.ecommerce_order_tracking_system.dto.request.CreateOrderReqDto;
import com.example.ecommerce_order_tracking_system.dto.response.OrderResDto;
import com.example.ecommerce_order_tracking_system.entity.order.CartItems;
import com.example.ecommerce_order_tracking_system.entity.order.Carts;
import com.example.ecommerce_order_tracking_system.entity.product.ProductVariants;
import com.example.ecommerce_order_tracking_system.entity.user.Users;
import com.example.ecommerce_order_tracking_system.repository.order.CartItemsRepo;
import com.example.ecommerce_order_tracking_system.repository.order.CartsRepo;
import com.example.ecommerce_order_tracking_system.repository.product.InventoriesRepo;
import com.example.ecommerce_order_tracking_system.repository.user.UsersRepo;
import com.example.ecommerce_order_tracking_system.service.OrdersService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrdersServiceImpl implements OrdersService {

    private final UsersRepo usersRepo;

    private final CartsRepo cartsRepo;

    private final CartItemsRepo cartItemsRepo;

    private final InventoriesRepo inventoriesRepo;

    @Override
    @Transactional
    public OrderResDto createOrder(String username, CreateOrderReqDto request) {
        if(username == null || request.getCarrierId() == null
        || request.getPaymentId() == null){
            throw new RuntimeException("Invalid parameters");
        }

        Optional<Users> user = usersRepo.findByUsername(username);
        if(user.isEmpty()){
            throw new RuntimeException("user not found!");
        }

        String userId = user.get().getId();

        Optional<Carts> cartsOptional = cartsRepo.getByUserId(userId);
        if (cartsOptional.isEmpty()) {
            throw new RuntimeException("Cart is empty!");
        }

        OrderResDto orderResDto = new OrderResDto();

        for(CartItems cartItems : cartsOptional.get().getCartItems()){
            if(Boolean.FALSE.equals(cartItems.getDeleted())) {
                ProductVariants productVariants = cartItems.getProductVariant();

                if (productVariants == null || productVariants.getProduct() == null) {
                    continue;
                }
                int updateStatus = inventoriesRepo.reserve(productVariants.getId(), cartItems.getQuantity());

                if(updateStatus == 0){
                    throw new RuntimeException("Out of stock!");
                }

                //mai code not phan nay
            }
        }

        return orderResDto;
    }
}
