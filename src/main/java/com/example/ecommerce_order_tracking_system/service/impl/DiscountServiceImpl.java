package com.example.ecommerce_order_tracking_system.service.impl;

import com.example.ecommerce_order_tracking_system.common.enums.UserVoucherStatus;
import com.example.ecommerce_order_tracking_system.dto.response.ListUserDiscountResDto;
import com.example.ecommerce_order_tracking_system.dto.response.UserDiscountResDto;
import com.example.ecommerce_order_tracking_system.entity.order.UserVouchers;
import com.example.ecommerce_order_tracking_system.repository.order.UserVouchersRepo;
import com.example.ecommerce_order_tracking_system.service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService {

    private final UserVouchersRepo userVouchersRepo;

    @Override
    public ListUserDiscountResDto getUserDiscount(String userId) {

        List<UserVouchers> userVouchersList = userVouchersRepo.getUserVoucherWithDetail(userId);
        if (userVouchersList.isEmpty()) {
            throw new RuntimeException("No available vouchers!");
        }

        List<UserDiscountResDto> userDiscountResDtoList = new ArrayList<>();

        for(UserVouchers vouchers : userVouchersList){
            if(Boolean.FALSE.equals(vouchers.getDeleted()) && vouchers.getStatus() == UserVoucherStatus.UNUSED){
                UserDiscountResDto userDiscountResDto = createUserDiscountResDto(vouchers);
                userDiscountResDtoList.add(userDiscountResDto);
            }
        }
        return new ListUserDiscountResDto(userDiscountResDtoList);
    }

    private static @NonNull UserDiscountResDto createUserDiscountResDto(UserVouchers vouchers) {
        UserDiscountResDto userDiscountResDto = new UserDiscountResDto();

        userDiscountResDto.setVoucherCode(vouchers.getVoucher().getVoucherCode());
        userDiscountResDto.setDiscountValue(vouchers.getVoucher().getDiscountValue());
        userDiscountResDto.setMinOrderAmount(vouchers.getVoucher().getMinOrderAmount());
        userDiscountResDto.setDescription(vouchers.getVoucher().getDescription());
        userDiscountResDto.setVoucherType(vouchers.getVoucher().getVoucherType());
        userDiscountResDto.setVoucherStatus(vouchers.getVoucher().getStatus());
        return userDiscountResDto;
    }
}
