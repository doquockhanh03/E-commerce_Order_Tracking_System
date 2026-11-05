package com.example.ecommerce_order_tracking_system.dto.request;

import com.example.ecommerce_order_tracking_system.entity.product.ProductVariants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AddToCartReqDto {
    private ProductVariants productVariantsId;

    private Integer quantity;
}
