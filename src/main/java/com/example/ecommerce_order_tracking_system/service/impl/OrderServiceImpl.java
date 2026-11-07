package com.example.ecommerce_order_tracking_system.service.impl;

import com.example.ecommerce_order_tracking_system.common.enums.DiscountType;
import com.example.ecommerce_order_tracking_system.common.enums.ShippingZone;
import com.example.ecommerce_order_tracking_system.common.enums.UserVoucherStatus;
import com.example.ecommerce_order_tracking_system.common.enums.VoucherStatus;
import com.example.ecommerce_order_tracking_system.dto.response.CartItemsResDto;
import com.example.ecommerce_order_tracking_system.dto.response.OrderSummaryResDto;
import com.example.ecommerce_order_tracking_system.entity.order.*;
import com.example.ecommerce_order_tracking_system.entity.product.ProductVariants;
import com.example.ecommerce_order_tracking_system.entity.user.Addresses;
import com.example.ecommerce_order_tracking_system.entity.user.Users;
import com.example.ecommerce_order_tracking_system.repository.order.CarriersRepo;
import com.example.ecommerce_order_tracking_system.repository.order.CartsRepo;
import com.example.ecommerce_order_tracking_system.repository.order.ShippingConfigsRepo;
import com.example.ecommerce_order_tracking_system.repository.order.UserVouchersRepo;
import com.example.ecommerce_order_tracking_system.repository.user.UsersRepo;
import com.example.ecommerce_order_tracking_system.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final UsersRepo usersRepo;

    private final CartsRepo cartsRepo;

    private final CarriersRepo carriersRepo;

    private final UserVouchersRepo userVouchersRepo;

    @Override
    public OrderSummaryResDto getOrderSummary(String username, String discountVoucher, String shippingVoucher) {
        Optional<Users> user = usersRepo.findByUsername(username);
        if(user.isEmpty()){
            throw new RuntimeException("user not found!");
        }
        String userId = user.get().getId();

        List<CartItems> cartItems = new ArrayList<>();

        Optional<Carts> cartsOptional = cartsRepo.getByUserId(userId);
        if (cartsOptional.isPresent()) {
            cartItems = cartsOptional.get().getCartItems();
        }

        return calculateOrderSummary(userId, cartItems, discountVoucher, shippingVoucher);
    }

    public OrderSummaryResDto calculateOrderSummary(String userId, List<CartItems> items, String discountVoucherId, String shippingVoucherId){
        BigDecimal subTotal = BigDecimal.ZERO;
        BigDecimal bulkDiscount = BigDecimal.ZERO;
        BigDecimal expeditedShipping = BigDecimal.ZERO;
        BigDecimal shippingDiscount = BigDecimal.ZERO;
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItems item : items){
            ProductVariants productVariants = item.getProductVariant();
            subTotal = subTotal.add(productVariants.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        expeditedShipping = expeditedShipping.add(calcShipping(items, userId));

        bulkDiscount = bulkDiscount.add(calcBulkDiscount(userId, discountVoucherId, subTotal);)

    }

    public BigDecimal calcBulkDiscount (String userId,
                                        String discountVoucherId, BigDecimal subTotal){

        Optional<UserVouchers> vouchers = userVouchersRepo.findByUserIdAndVoucherId(userId, discountVoucherId);

        BigDecimal discountVal = BigDecimal.ZERO;
        BigDecimal bulkDiscount = BigDecimal.ZERO;
        if(vouchers.isPresent()){
            if(Boolean.FALSE.equals(vouchers.get().getDeleted()) && vouchers.get().getStatus() == UserVoucherStatus.UNUSED){
                if(vouchers.get().getVoucher().getMinOrderAmount().compareTo(subTotal) <= 0
                        && (vouchers.get().getVoucher().getUsageLimit() - vouchers.get().getVoucher().getUsageCount()) > 0
                        && vouchers.get().getVoucher().getStatus() == VoucherStatus.ACTIVE){

                    discountVal = discountVal.add(vouchers.get().getVoucher().getDiscountValue());

                    if(vouchers.get().getVoucher().getDiscountType() == DiscountType.FIXED_AMOUNT){
                        bulkDiscount = discountVal.min(subTotal);

                    }else if (vouchers.get().getVoucher().getDiscountType() == DiscountType.PERCENTAGE){
                        BigDecimal temp = subTotal
                                .multiply(discountVal)
                                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

                        if (temp.compareTo(vouchers.get().getVoucher().getMaxDiscountAmount()) > 0) {
                            bulkDiscount = vouchers.get().getVoucher().getMaxDiscountAmount();
                        }else {
                            bulkDiscount = temp.min(subTotal);
                        }
                    }
                }
            }
        }
        return bulkDiscount;
    }

    public BigDecimal calcShipping (List<CartItems> items, String userId){
        Optional<Users> usersOptional = usersRepo.findById(userId);
        String address = "";
        if(usersOptional.isPresent()){
            for(Addresses addresses : usersOptional.get().getAddresses()){
                if(addresses.getIsDefault()){
                    address = addresses.getProvinceCity();
                    break;
                }
            }
        }else {
            throw new RuntimeException("address not found!");
        }

        ShippingZone zone;
        if(address.equalsIgnoreCase("Ha Noi") || address.equalsIgnoreCase("Ho Chi Minh")){
            zone = ShippingZone.NOI_THANH;
        }else{
            zone = ShippingZone.LIEN_TINH;
        }

        ShippingConfigs configs = carriersRepo.findByMinFee(zone);

        BigDecimal shippingFee = BigDecimal.ZERO;
        for (CartItems item : items){
            ProductVariants productVariants = item.getProductVariant();
            shippingFee = shippingFee.add(productVariants.getWeight().multiply(configs.getPrice()))
                    .multiply(BigDecimal.valueOf(item.getQuantity()));
        }

        return shippingFee;
    }
}
