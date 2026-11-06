package com.example.ecommerce_order_tracking_system.service.impl;

import com.example.ecommerce_order_tracking_system.common.enums.ShippingZone;
import com.example.ecommerce_order_tracking_system.dto.response.CartItemsResDto;
import com.example.ecommerce_order_tracking_system.dto.response.OrderSummaryResDto;
import com.example.ecommerce_order_tracking_system.entity.order.*;
import com.example.ecommerce_order_tracking_system.entity.product.ProductVariants;
import com.example.ecommerce_order_tracking_system.entity.user.Addresses;
import com.example.ecommerce_order_tracking_system.entity.user.Users;
import com.example.ecommerce_order_tracking_system.repository.order.CarriersRepo;
import com.example.ecommerce_order_tracking_system.repository.order.CartsRepo;
import com.example.ecommerce_order_tracking_system.repository.order.ShippingConfigsRepo;
import com.example.ecommerce_order_tracking_system.repository.user.UsersRepo;
import com.example.ecommerce_order_tracking_system.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final UsersRepo usersRepo;

    private final CartsRepo cartsRepo;

    private final CarriersRepo carriersRepo;

    private final ShippingConfigsRepo shippingConfigsRepo;

    @Override
    public OrderSummaryResDto getOrderSummary(String username) {
        Optional<Users> user = usersRepo.findByUsername(username);
        if(user.isEmpty()){
            throw new RuntimeException("user not found!");
        }
        String userId = user.get().getId();

        List<CartItems> cartItems = new ArrayList<>();
        Vouchers discountVoucher = null;
        Vouchers shippingVoucher = null;

        Optional<Carts> cartsOptional = cartsRepo.getByUserId(userId);
        if (cartsOptional.isPresent()) {
            cartItems = cartsOptional.get().getCartItems();
        }

        return calculateOrderSummary(userId,cartItems, discountVoucher, shippingVoucher);
    }

    public OrderSummaryResDto calculateOrderSummary(String userId, List<CartItems> items, Vouchers discountVoucher, Vouchers shippingVoucher){
        BigDecimal subTotal = BigDecimal.ZERO;
        BigDecimal bulkDiscount = BigDecimal.ZERO;
        BigDecimal expeditedShipping = BigDecimal.ZERO;
        BigDecimal shippingDiscount = BigDecimal.ZERO;
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItems item : items){
            ProductVariants productVariants = item.getProductVariant();
            subTotal = subTotal.add(productVariants.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

    }

    public BigDecimal calcShipping (List<CartItems> items, String userId, String carrierId){
        Optional<Users> usersOptional = usersRepo.findById(userId);
        String address = "";
        if(usersOptional.isPresent()){
            for(Addresses addresses : usersOptional.get().getAddresses()){
                if(addresses.getIsDefault()){
                    address = addresses.getProvinceCity();
                    break;
                }
            }
        }

        ShippingZone zone;
        if(address.equalsIgnoreCase("Ha Noi") || address.equalsIgnoreCase("Ho Chi Minh")){
            zone = ShippingZone.NOI_THANH;
        }else{
            zone = ShippingZone.LIEN_TINH;
        }

        List<ShippingConfigs> configs = carriersRepo.findByMinFee(zone);

        BigDecimal minShippingFee = configs.getFirst().getPrice();

        for(ShippingConfigs config: configs){
            if(config.getPrice().compareTo(minShippingFee) < 0){
                minShippingFee = config.getPrice();
            }
        }
        return minShippingFee;
    }
}
