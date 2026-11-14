package com.example.ecommerce_order_tracking_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CartItemsResDto {

    private String productName;

    private String VariantName;

    private Integer quantity;

    private String sku;

    private String imageUrl;

    private Integer quantityVariantInStock;

    private BigDecimal totalPrice;

    private String inventoryStatus;
}
