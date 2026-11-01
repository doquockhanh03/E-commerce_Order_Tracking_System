package com.example.ecommerce_order_tracking_system.exception;

import com.example.ecommerce_order_tracking_system.common.enums.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{
    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode, String message){
        super(message);
        this.errorCode = errorCode;
    }
}
