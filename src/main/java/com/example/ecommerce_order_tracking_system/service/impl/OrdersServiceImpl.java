package com.example.ecommerce_order_tracking_system.service.impl;

import com.example.ecommerce_order_tracking_system.common.enums.*;
import com.example.ecommerce_order_tracking_system.dto.request.CreateOrderReqDto;
import com.example.ecommerce_order_tracking_system.dto.response.OrderResDto;
import com.example.ecommerce_order_tracking_system.dto.response.PricingSummaryResDto;
import com.example.ecommerce_order_tracking_system.entity.order.*;
import com.example.ecommerce_order_tracking_system.entity.product.Inventories;
import com.example.ecommerce_order_tracking_system.entity.product.ProductVariants;
import com.example.ecommerce_order_tracking_system.entity.user.Addresses;
import com.example.ecommerce_order_tracking_system.entity.user.Shops;
import com.example.ecommerce_order_tracking_system.entity.user.Users;
import com.example.ecommerce_order_tracking_system.repository.order.*;
import com.example.ecommerce_order_tracking_system.repository.product.InventoriesRepo;
import com.example.ecommerce_order_tracking_system.repository.user.AddressesRepo;
import com.example.ecommerce_order_tracking_system.repository.user.UsersRepo;
import com.example.ecommerce_order_tracking_system.service.OrdersService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrdersServiceImpl implements OrdersService {

    private final UsersRepo usersRepo;
    private final CartsRepo cartsRepo;
    private final InventoriesRepo inventoriesRepo;
    private final PricingServiceImpl pricingService;
    private final AddressesRepo addressesRepo;
    private final CarriersRepo carriersRepo;
    private final PaymentMethodsRepo paymentMethodsRepo;
    private final UserVouchersRepo userVouchersRepo;
    private final OrdersRepo ordersRepo;
    private final OrderItemsRepo orderItemsRepo;
    private final PaymentsRepo paymentsRepo;
    private final VouchersRepo vouchersRepo;
    private final OrderVouchersRepo orderVouchersRepo;
    private final TrackingLogsRepo trackingLogsRepo;
    private final CartItemsRepo cartItemsRepo;

    @Override
    @Transactional
    public OrderResDto createOrder(String userId, List<String> cartItemIds, CreateOrderReqDto request) {
        if(userId == null || request.getCarrierId() == null
                || request.getPaymentMethodId() == null){
            throw new RuntimeException("Invalid parameters");
        }

        Optional<Users> usersOptional = usersRepo.findById(userId);
        if(usersOptional.isEmpty()){
            throw new RuntimeException("user not found!");
        }

        Users user = usersOptional.get();

        Optional<Addresses> address;
        if (request.getAddressId() != null && !request.getAddressId().isBlank()) {
            address = addressesRepo.findByIdAndUserId(request.getAddressId(), userId);
            if(address.isEmpty()){
                throw new RuntimeException("Address not found!");
            }
        } else {
            address = addressesRepo.findFirstByUserIdAndIsDefaultTrueAndDeletedFalse(userId);
            if(address.isEmpty()){
                throw new RuntimeException("Address not found!");
            }
        }

        Optional<Carriers> carriersOptional = carriersRepo.findById(request.getCarrierId());
        if(carriersOptional.isEmpty()){
            throw new RuntimeException("Carrier not found!");
        }
        Carriers carrier = carriersOptional.get();


        Optional<PaymentMethods> paymentMethods = paymentMethodsRepo.findById(request.getPaymentMethodId());
        if(paymentMethods.isEmpty()){
            throw new RuntimeException("Payment method not found!");
        }

        Optional<UserVouchers> discountVoucher = Optional.empty();
        Optional<UserVouchers> shippingVoucher = Optional.empty();

        if(request.getDiscountVoucherId() != null && !request.getDiscountVoucherId().isBlank()){
            Optional<Vouchers> vouchers = vouchersRepo.findById(request.getDiscountVoucherId());

            if(vouchers.isPresent() && vouchers.get().getVoucherType() == VoucherType.DISCOUNT){
                if(vouchers.get().getStatus() == VoucherStatus.EXPIRED){
                    throw new RuntimeException("Voucher has expired!");
                }

                int limit = vouchers.get().getUsageLimit() - vouchers.get().getUsageCount();
                if(limit <= 0){
                    throw new RuntimeException("Voucher is no longer available!");
                }
            }else {
                throw new RuntimeException("Discount voucher not found!");
            }

            discountVoucher = userVouchersRepo.findByUserIdAndVoucherId(userId, request.getDiscountVoucherId());
            if(discountVoucher.isEmpty() || Boolean.TRUE.equals(discountVoucher.get().getDeleted())){
                throw new RuntimeException("Discount voucher not found!");
            }

            if (discountVoucher.get().getStatus() != UserVoucherStatus.UNUSED) {
                throw new RuntimeException("Discount voucher already used!");
            }
        }

        if(request.getShippingVoucherId() != null && !request.getShippingVoucherId().isBlank()){
            Optional<Vouchers> vouchers = vouchersRepo.findById(request.getShippingVoucherId());
            if(vouchers.isPresent() && vouchers.get().getVoucherType() == VoucherType.FREE_SHIPPING){

                if(vouchers.get().getStatus() == VoucherStatus.EXPIRED){
                    throw new RuntimeException("Voucher has expired!");
                }

                int limit = vouchers.get().getUsageLimit() - vouchers.get().getUsageCount();
                if(limit <= 0){
                    throw new RuntimeException("Voucher is no longer available!");
                }
            }else {
                throw new RuntimeException("Discount voucher not found!");
            }

            shippingVoucher = userVouchersRepo.findByUserIdAndVoucherId(userId, request.getShippingVoucherId());
            if(shippingVoucher.isEmpty() || Boolean.TRUE.equals(shippingVoucher.get().getDeleted())){
                throw new RuntimeException("Shipping voucher not found!");
            }

            if (shippingVoucher.get().getStatus() != UserVoucherStatus.UNUSED) {
                throw new RuntimeException("Discount voucher already used!");
            }
        }

        Optional<Carts> cartsOptional = cartsRepo.getByUserId(userId);
        if (cartsOptional.isEmpty()) {
            throw new RuntimeException("Cart is empty!");
        }

        List<CartItems> cartItems = cartItemsRepo.getSelectedItems(cartsOptional.get().getId(), cartItemIds);

        for(CartItems items : cartItems){
            if(Boolean.FALSE.equals(items.getDeleted())) {
                ProductVariants productVariants = items.getProductVariant();

                Optional<Inventories> inventories = inventoriesRepo.findByProductVariantIdForUpdate(productVariants.getId());
                if(inventories.isEmpty()){
                    throw new RuntimeException("Product variant not found!");
                }

                if(inventories.get().getQuantityInStock() - inventories.get().getReservedQuantity() < items.getQuantity()){
                    throw new RuntimeException("Out of stock!");
                }

                inventories.get().setReservedQuantity(inventories.get().getReservedQuantity() + items.getQuantity());
            }
        }
        List<String> listOrderIds = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        PricingSummaryResDto pricingAll = pricingService
                .calculateOrderSummary(userId, cartItems, address.get().getProvinceCity(), request.getDiscountVoucherId(), request.getShippingVoucherId(), null);

        BigDecimal totalDiscount = pricingAll.getBulkDiscount();
        BigDecimal totalSubtotal = pricingAll.getSubTotal();
        BigDecimal totalShippingDiscount = pricingAll.getShippingDiscount();
        BigDecimal totalShippingFee = pricingAll.getExpeditedShipping();

        if (discountVoucher.isPresent()
                && pricingAll.getSubTotal().compareTo(discountVoucher.get().getVoucher().getMinOrderAmount()) < 0) {
            throw new RuntimeException("Order does not meet discount voucher minimum!");
        }

        if (shippingVoucher.isPresent()
                && pricingAll.getSubTotal().compareTo(shippingVoucher.get().getVoucher().getMinOrderAmount()) < 0) {
            throw new RuntimeException("Order does not meet shipping voucher minimum!");
        }

        List<String> shopIds = new ArrayList<>();
        for(CartItems items : cartItems){
            if(Boolean.FALSE.equals(items.getDeleted())) {
                String shopId = items.getProductVariant().getProduct().getShop().getId();
                if(!shopIds.contains(shopId)){
                    shopIds.add(shopId);
                }
            }
        }

        if(shopIds.isEmpty()){
            throw new RuntimeException("Cart is empty!");
        }

        BigDecimal allShippingDiscount = BigDecimal.ZERO;
        BigDecimal allDiscount = BigDecimal.ZERO;

        for(int i = 0; i < shopIds.size(); i++){
            List<CartItems> shopItems = new ArrayList<>();

            for(CartItems items : cartItems){
                if(Boolean.FALSE.equals(items.getDeleted())) {
                    String shopIdCheck = items.getProductVariant().getProduct().getShop().getId();
                    if (shopIds.get(i).equals(shopIdCheck)){
                        shopItems.add(items);
                    }
                }
            }

            Shops shop = shopItems.getFirst().getProductVariant().getProduct().getShop();

            BigDecimal shopDiscount;
            BigDecimal shopShippingDiscount;

            PricingSummaryResDto pricingShop = pricingService.calculateOrderSummary(
                    userId, shopItems, address.get().getProvinceCity(), "", "", null);

            if(i == shopIds.size() - 1){
                shopDiscount = totalDiscount.subtract(allDiscount);
                shopShippingDiscount = totalShippingDiscount.subtract(allShippingDiscount);
            }else{
                shopDiscount = (totalSubtotal.signum() > 0)
                        ? pricingShop.getSubTotal().multiply(totalDiscount)
                        .divide(totalSubtotal, 2, RoundingMode.HALF_UP)
                        : BigDecimal.ZERO;

                allDiscount = allDiscount.add(shopDiscount);

                shopShippingDiscount = (totalShippingFee.signum() > 0)
                        ? pricingShop.getExpeditedShipping().multiply(totalShippingDiscount)
                        .divide(totalShippingFee, 2, RoundingMode.HALF_UP)
                        : BigDecimal.ZERO;
                allShippingDiscount = allShippingDiscount.add(shopShippingDiscount);
            }

            pricingShop.setBulkDiscount(shopDiscount);
            pricingShop.setShippingDiscount(shopShippingDiscount);

            pricingShop.setTotalAmount(pricingShop.getSubTotal()
                    .subtract(shopDiscount)
                    .add(pricingShop.getExpeditedShipping())
                    .subtract(shopShippingDiscount));

            String shippingAddress = address.get().getAddressDetail() + ", "
                    + address.get().getHamletVillage() + ", "
                    + address.get().getWardCommune() + ", "
                    + address.get().getProvinceCity();

            Orders orders = new Orders();

            orders.setUser(user);
            orders.setShop(shop);
            orders.setCarrier(carrier);
            orders.setTrackingNumber(randomTrackingNumber());
            orders.setRecipientName(address.get().getRecipientName());
            orders.setRecipientPhone(address.get().getPhoneNumber());
            orders.setShippingAddress(shippingAddress);
            orders.setNote(request.getNote());
            orders.setSubtotal(pricingShop.getSubTotal());
            orders.setDiscountAmount(pricingShop.getBulkDiscount());
            orders.setShippingFee(pricingShop.getExpeditedShipping());
            orders.setShippingDiscountAmount(pricingShop.getShippingDiscount());
            orders.setTotalAmount(pricingShop.getTotalAmount());
            orders.setEstimatedDeliveryAt(Instant.now().plus(5, ChronoUnit.DAYS));
            orders.setStatus(OrderStatus.PENDING);
            orders.setPaymentStatus(OrderPaymentStatus.AWAITING_PAYMENT);
            orders.setCreatedBy(userId);
            orders.setCreatedAt(Instant.now());
            orders.setDeleted(false);

            Orders orderSave =  ordersRepo.save(orders);

            discountVoucher.ifPresent(userVouchers -> orderVouchersRepo.save(setOderVouchers(orderSave, userVouchers.getVoucher())));

            shippingVoucher.ifPresent(userVouchers -> orderVouchersRepo.save(setOderVouchers(orderSave, userVouchers.getVoucher())));


            for(CartItems items : shopItems){
                ProductVariants variants = items.getProductVariant();

                OrderItems orderItems = new OrderItems();

                orderItems.setOrder(orderSave);
                orderItems.setProductVariant(variants);
                orderItems.setProductName(variants.getProduct().getName());
                orderItems.setVariantName(variants.getName());
                orderItems.setSku(variants.getSku());
                orderItems.setQuantity(items.getQuantity());
                orderItems.setUnitPrice(variants.getPrice());
                orderItems.setCreatedAt(Instant.now());
                orderItems.setCreatedBy(userId);
                orderItems.setDeleted(false);

                orderItemsRepo.save(orderItems);
            }

            Payments payments = new Payments();

            payments.setOrder(orderSave);
            payments.setPaymentMethod(paymentMethods.get());
            payments.setAmount(pricingShop.getTotalAmount());
            payments.setTransactionReference("test12345");
            payments.setPaymentGateway("test23456");
            payments.setCreatedAt(Instant.now());
            payments.setCreatedBy("SYSTEM");

            if(paymentMethods.get().getName().equals("COD")){
                payments.setStatus(PaymentStatus.UNPAID);
            }else {
                payments.setStatus(PaymentStatus.AWAITING_PAYMENT);
            }
            paymentsRepo.save(payments);

            TrackingLogs trackingLogs = new TrackingLogs();

            trackingLogs.setOrder(orderSave);
            trackingLogs.setDescription("Da tao don hang!");
            trackingLogs.setStatus(OrderStatus.PENDING);
            trackingLogs.setLocationName("Kho hang");
            trackingLogs.setCreatedAt(Instant.now());
            trackingLogs.setCreatedBy("SYSTEM");

            trackingLogsRepo.save(trackingLogs);

            listOrderIds.add(orderSave.getId());
            totalAmount = totalAmount.add(pricingShop.getTotalAmount());

        }

        discountVoucher.ifPresent(userVouchers -> setVoucherUsed(userId, userVouchers));

        shippingVoucher.ifPresent(userVouchers -> setVoucherUsed(userId, userVouchers));

        OrderResDto orderResDto = new OrderResDto();

        orderResDto.setOrderIds(listOrderIds);
        orderResDto.setTotalAmount(totalAmount);

        return orderResDto;
    }

    private void setVoucherUsed(String userId, UserVouchers userVouchers){
        if(userVouchers != null){
            userVouchers.setStatus(UserVoucherStatus.USED);
            userVouchers.setUpdatedAt(Instant.now());
            userVouchers.setUpdatedBy(userId);
            userVouchersRepo.save(userVouchers);

            Vouchers vouchers = userVouchers.getVoucher();
            vouchers.setUsageCount(vouchers.getUsageCount() + 1);
            vouchersRepo.save(vouchers);
        }
    }

    private OrderVouchers setOderVouchers(Orders orders, Vouchers vouchers){
        OrderVouchers orderVouchers = new OrderVouchers();

        orderVouchers.setOrder(orders);
        orderVouchers.setVoucher(vouchers);
        orderVouchers.setCreatedAt(Instant.now());
        orderVouchers.setCreatedBy("SYSTEM");
        orderVouchers.setDeleted(false);

        return orderVouchers;
    }

    private static final SecureRandom TrkRandom = new SecureRandom();

    public String randomTrackingNumber() {
        StringBuilder trackingNumber = new StringBuilder("TRK");

        for (int i = 0; i < 4; i++) {
            trackingNumber.append(TrkRandom.nextInt(10));
        }

        for (int i = 0; i < 2; i++) {
            trackingNumber.append((char) ('A' + TrkRandom.nextInt(26)));
        }

        for (int i = 0; i < 3; i++) {
            trackingNumber.append(TrkRandom.nextInt(10));
        }

        return trackingNumber.toString();
    }
}
