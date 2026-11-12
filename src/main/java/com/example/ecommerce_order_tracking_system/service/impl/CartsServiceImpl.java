package com.example.ecommerce_order_tracking_system.service.impl;

import com.example.ecommerce_order_tracking_system.dto.request.AddToCartReqDto;
import com.example.ecommerce_order_tracking_system.dto.response.CartItemsResDto;
import com.example.ecommerce_order_tracking_system.dto.response.CartsResDto;
import com.example.ecommerce_order_tracking_system.entity.order.CartItems;
import com.example.ecommerce_order_tracking_system.entity.order.Carts;
import com.example.ecommerce_order_tracking_system.entity.product.ProductImages;
import com.example.ecommerce_order_tracking_system.entity.product.ProductVariants;
import com.example.ecommerce_order_tracking_system.repository.order.CartItemsRepo;
import com.example.ecommerce_order_tracking_system.repository.order.CartsRepo;
import com.example.ecommerce_order_tracking_system.service.CartsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartsServiceImpl implements CartsService {
    private final CartsRepo cartsRepo;

    private final CartItemsRepo cartItemsRepo;

    @Override
    public void addToCart(AddToCartReqDto request) {

    }

    @Override
    @Transactional
    public CartsResDto updateCartItemQuantity(String userId, String cartItemId, String action) {
        if(cartItemId == null || cartItemId.isBlank() || action == null || action.isBlank()){
            throw new IllegalArgumentException("Invalid parameters");
        }

        Optional<Carts> cartsOptional = cartsRepo.getByUserId(userId);
        if (cartsOptional.isEmpty()) {
            throw new RuntimeException("Cart is empty!");
        }

        Optional<CartItems> cartItemsOptional = cartItemsRepo.findById(cartItemId);
        if (cartItemsOptional.isEmpty()) {
            throw new RuntimeException("Cart item not found!");
        }

        if(!cartItemsOptional.get().getCart().getId().equals(cartsOptional.get().getId())){
            throw new RuntimeException("Cart item not match!");
        }

        int quantityInStock = cartItemsOptional.get().getProductVariant().getInventory().getQuantityInStock();
        int reservedQuantity = cartItemsOptional.get().getProductVariant().getInventory().getReservedQuantity();
        int itemQuantity = cartItemsOptional.get().getQuantity();

        if(action.equalsIgnoreCase("INCREASE")){
            if((quantityInStock - reservedQuantity) > itemQuantity){
                cartItemsOptional.get().setQuantity(cartItemsOptional.get().getQuantity() + 1);
            }
        }else if(action.equalsIgnoreCase("DECREASE")){
            if(itemQuantity > 1){
                cartItemsOptional.get().setQuantity(cartItemsOptional.get().getQuantity() - 1);
            }
        }else {
            throw new IllegalArgumentException("Invalid action!");
        }

        return getCartDetail(userId);
    }

    @Override
    public CartsResDto getCartDetail(String userId) {

        if(userId == null || userId.isBlank()){
            throw new RuntimeException("User not found!");
        }

        Optional<Carts> cartsOptional = cartsRepo.getByUserId(userId);
        if (cartsOptional.isEmpty()) {
            throw new RuntimeException("Cart is empty!");
        }

        List<CartItems> cartItemsList = cartItemsRepo.getCartItemsWithDetails(cartsOptional.get().getId());

        List<CartItemsResDto> cartItemsResDto = new ArrayList<>();

        for(CartItems items : cartItemsList){
            cartItemsResDto.add(createCartItemsResDto(items, items.getProductVariant()));
        }

        return new CartsResDto(cartItemsResDto);
    }

    private CartItemsResDto createCartItemsResDto(CartItems items, ProductVariants productVariants) {
        CartItemsResDto cartItemsResDto = new CartItemsResDto();

        cartItemsResDto.setProductName(productVariants.getProduct().getName());
        cartItemsResDto.setVariantName(productVariants.getName());
        cartItemsResDto.setQuantity(items.getQuantity());
        cartItemsResDto.setSku(productVariants.getSku());

        BigDecimal totalPrice = productVariants.getPrice().multiply(new BigDecimal(items.getQuantity()));
        cartItemsResDto.setTotalPrice(totalPrice);

        Integer quantityInStock = (productVariants.getInventory().getQuantityInStock() - productVariants.getInventory().getReservedQuantity());
        cartItemsResDto.setQuantityVariantInStock(quantityInStock);

        boolean imgCheck = false;
        for(ProductImages images : productVariants.getProductImages()){
            if(Boolean.FALSE.equals(images.getDeleted()) && images.getDisplayOrder() == 1){
                cartItemsResDto.setImageUrl(images.getImageUrl());
                imgCheck = true;
                break;
            }
        }
        if(!imgCheck){
            cartItemsResDto.setImageUrl("default-image.png");
        }
        return cartItemsResDto;
    }


}
