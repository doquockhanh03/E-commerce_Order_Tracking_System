package com.example.ecommerce_order_tracking_system.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ExceptionResponse {
    private Integer status;
    private String errorCode;
    private String message;
    private LocalDateTime timestamp;
    private String path;
    private Map<String, String> errors;

    public ExceptionResponse(int status, String errorCode, String message) {
        this(status, errorCode, message, null);
    }

    public ExceptionResponse(int status, String errorCode, String message, Map<String, String> errors) {
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
        this.errors = errors;
        this.timestamp = LocalDateTime.now();
    }
}
