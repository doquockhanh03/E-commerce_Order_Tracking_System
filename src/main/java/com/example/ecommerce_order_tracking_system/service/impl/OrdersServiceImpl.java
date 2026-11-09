package com.example.ecommerce_order_tracking_system.service.impl;

import com.example.ecommerce_order_tracking_system.common.enums.OrderPaymentStatus;
import com.example.ecommerce_order_tracking_system.common.enums.OrderStatus;
import com.example.ecommerce_order_tracking_system.common.enums.PaymentStatus;
import com.example.ecommerce_order_tracking_system.dto.request.CreateOrderReqDto;
import com.example.ecommerce_order_tracking_system.dto.response.OrderResDto;
import com.example.ecommerce_order_tracking_system.dto.response.PricingSummaryResDto;
import com.example.ecommerce_order_tracking_system.entity.order.*;
import com.example.ecommerce_order_tracking_system.entity.product.ProductVariants;
import com.example.ecommerce_order_tracking_system.entity.user.Addresses;
import com.example.ecommerce_order_tracking_system.entity.user.Shops;
import com.example.ecommerce_order_tracking_system.entity.user.Users;
import com.example.ecommerce_order_tracking_system.repository.order.*;
import com.example.ecommerce_order_tracking_system.repository.product.InventoriesRepo;
import com.example.ecommerce_order_tracking_system.repository.user.AddressesRepo;
import com.example.ecommerce_order_tracking_system.repository.user.UsersRepo;
import com.example.ecommerce_order_tracking_system.service.OrdersService;
import com.example.ecommerce_order_tracking_system.service.PricingService;
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

    private final CartItemsRepo cartItemsRepo;

    private final InventoriesRepo inventoriesRepo;

    private final PricingServiceImpl pricingService;
    private final AddressesRepo addressesRepo;
    private final CarriersRepo carriersRepo;
    private final PaymentMethodsRepo paymentMethodsRepo;
    private final UserVouchersRepo userVouchersRepo;
    private final OrdersRepo ordersRepo;
    private final OrderItemsRepo orderItemsRepo;
    private final PaymentsRepo paymentsRepo;

    @Override
    @Transactional
    public OrderResDto createOrder(String username, CreateOrderReqDto request) {
        if(username == null || request.getCarrierId() == null
        || request.getPaymentMethodId() == null){
            throw new RuntimeException("Invalid parameters");
        }

        Optional<Users> usersOptional = usersRepo.findByUsername(username);
        if(usersOptional.isEmpty()){
            throw new RuntimeException("user not found!");
        }

        String userId = usersOptional.get().getId();
        Users user = usersOptional.get();

        Optional<Addresses> address = addressesRepo.findById(request.getAddressId());
        if(address.isEmpty()){
            throw new RuntimeException("Address not found!");
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

        if(!request.getDiscountVoucherId().isBlank()){
            Optional<UserVouchers> vouchers = userVouchersRepo.findByUserIdAndVoucherId(userId, request.getDiscountVoucherId());
            if(vouchers.isEmpty()){
                throw new RuntimeException("Discount voucher not found!");
            }
        }else if(!request.getShippingVoucherId().isBlank()){
            Optional<UserVouchers> vouchers = userVouchersRepo.findByUserIdAndVoucherId(userId, request.getDiscountVoucherId());
            if(vouchers.isEmpty()){
                throw new RuntimeException("Shipping voucher not found!");
            }
        }

        Optional<Carts> cartsOptional = cartsRepo.getByUserId(userId);
        if (cartsOptional.isEmpty()) {
            throw new RuntimeException("Cart is empty!");
        }

        List<CartItems> cartItems = cartsOptional.get().getCartItems();

        for(CartItems items : cartItems){
            if(Boolean.FALSE.equals(items.getDeleted())) {
                ProductVariants productVariants = items.getProductVariant();

                if (productVariants == null || productVariants.getProduct() == null) {
                    continue;
                }
                int updateStatus = inventoriesRepo.reserve(productVariants.getId(), items.getQuantity());

                if(updateStatus == 0){
                    throw new RuntimeException("Out of stock!");
                }
            }
        }
        List<String> listOrderIds = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        PricingSummaryResDto pricingAll = pricingService
                .calculateOrderSummary(userId, cartItems, request.getDiscountVoucherId(), request.getShippingVoucherId());

        BigDecimal totalDiscount = pricingAll.getBulkDiscount();
        BigDecimal TotalSubtotal = pricingAll.getSubTotal();

        List<String> shopIds = new ArrayList<>();
        for(CartItems items : cartItems){

            String shopId = items.getProductVariant().getProduct().getShop().getId();
            if(!shopIds.contains(shopId)){
                shopIds.add(shopId);
            }
        }

        BigDecimal allDiscount = BigDecimal.ZERO;
        int index = shopIds.size();
        for(String shopId : shopIds){
            List<CartItems> shopItems = new ArrayList<>();

            for(CartItems items : cartItems){
                String shopIdCheck = items.getProductVariant().getProduct().getShop().getId();
                if (shopId.equals(shopIdCheck)){
                    shopItems.add(items);
                }
            }

            Shops shop = shopItems.get(0).getProductVariant().getProduct().getShop();

            BigDecimal shopDiscount;

            PricingSummaryResDto pricingShop = pricingService.calculateOrderSummary(
                    userId, shopItems, "", request.getShippingVoucherId());

            if(index == shopIds.size() - 1){
                shopDiscount = totalDiscount.subtract(allDiscount);
            }else{
                shopDiscount = pricingShop.getSubTotal()
                        .multiply(totalDiscount)
                        .divide(TotalSubtotal, 2, RoundingMode.HALF_UP);
            }

            pricingShop.setBulkDiscount(shopDiscount);

            pricingShop.setTotalAmount(pricingShop.getSubTotal()
                    .subtract(shopDiscount)
                    .add(pricingShop.getExpeditedShipping())
                    .subtract(pricingShop.getShippingDiscount()));

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

            Orders orderSave =  ordersRepo.save(orders);

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

                orderItemsRepo.save(orderItems);
            }

            Payments payments = new Payments();

            payments.setOrder(orderSave);
            payments.setPaymentMethod(paymentMethods.get());
            payments.setAmount(pricingShop.getTotalAmount());
            payments.setTransactionReference("test12345");
            payments.setPaymentGateway("test23456");

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
            trackingLogs.setCreatedAt(Instant.now());
            trackingLogs.setCreatedBy("SYSTEM");

            listOrderIds.add(orderSave.getId());

            totalAmount = totalAmount.add(pricingShop.getTotalAmount());

            index++;
        }

        OrderResDto orderResDto = new OrderResDto();

        orderResDto.setOrderIds(listOrderIds);
        orderResDto.setTotalAmount(totalAmount);

        return orderResDto;
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
