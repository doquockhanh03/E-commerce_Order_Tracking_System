package com.example.ecommerce_order_tracking_system.service.impl;

import com.example.ecommerce_order_tracking_system.common.enums.UserVoucherStatus;
import com.example.ecommerce_order_tracking_system.common.enums.VoucherStatus;
import com.example.ecommerce_order_tracking_system.dto.response.ListUserDiscountResDto;
import com.example.ecommerce_order_tracking_system.dto.response.UserDiscountResDto;
import com.example.ecommerce_order_tracking_system.entity.order.CartItems;
import com.example.ecommerce_order_tracking_system.entity.order.Carts;
import com.example.ecommerce_order_tracking_system.entity.order.UserVouchers;
import com.example.ecommerce_order_tracking_system.entity.product.ProductVariants;
import com.example.ecommerce_order_tracking_system.entity.user.Users;
import com.example.ecommerce_order_tracking_system.repository.order.CartsRepo;
import com.example.ecommerce_order_tracking_system.repository.order.UserVouchersRepo;
import com.example.ecommerce_order_tracking_system.repository.user.UsersRepo;
import com.example.ecommerce_order_tracking_system.service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService {

    private final UsersRepo usersRepo;

    private final UserVouchersRepo userVouchersRepo;

    private final CartsRepo cartsRepo;

    @Override
    public ListUserDiscountResDto getUserDiscount(String username) {
        Optional<Users> usersOptional = usersRepo.findByUsername(username);
        if (usersOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        String userId = usersOptional.get().getId();

        Optional<Carts> cartsOptional = cartsRepo.getByUserId(userId);
        if (cartsOptional.isEmpty()) {
            throw new RuntimeException("Cart not found");
        }

        BigDecimal totalPrice = BigDecimal.ZERO;
        for(CartItems cartItems : cartsOptional.get().getCartItems()){
            ProductVariants productVariants = cartItems.getProductVariant();

            totalPrice = totalPrice.add(productVariants.getPrice().multiply(BigDecimal.valueOf(cartItems.getQuantity())));
        }

        List<UserVouchers> userVouchersList = userVouchersRepo.findByUserId(userId);
        if (userVouchersList.isEmpty()) {
            throw new RuntimeException("Voucher not found");
        }

        List<UserDiscountResDto> userDiscountResDtoList = new ArrayList<>();

        for(UserVouchers vouchers : userVouchersList){
            if(Boolean.FALSE.equals(vouchers.getDeleted()) && vouchers.getStatus() == UserVoucherStatus.UNUSED){
                if(vouchers.getVoucher().getMinOrderAmount().compareTo(totalPrice) <= 0
                        && (vouchers.getVoucher().getUsageLimit() - vouchers.getVoucher().getUsageCount()) > 0
                        && vouchers.getVoucher().getStatus() == VoucherStatus.ACTIVE){

                    UserDiscountResDto userDiscountResDto = new UserDiscountResDto();

                    userDiscountResDto.setVoucherCode(vouchers.getVoucher().getVoucherCode());
                    userDiscountResDto.setDiscountValue(vouchers.getVoucher().getDiscountValue());
                    userDiscountResDto.setMinOrderAmount(vouchers.getVoucher().getMinOrderAmount());
                    userDiscountResDto.setDescription(vouchers.getVoucher().getDescription());
                    userDiscountResDto.setVoucherType(vouchers.getVoucher().getVoucherType());
                    userDiscountResDto.setVoucherStatus(vouchers.getVoucher().getStatus());

                    userDiscountResDtoList.add(userDiscountResDto);
                }
            }
        }
        return new ListUserDiscountResDto(userDiscountResDtoList);
    }
}
