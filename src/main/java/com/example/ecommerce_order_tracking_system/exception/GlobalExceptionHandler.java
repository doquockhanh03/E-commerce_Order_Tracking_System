package com.example.ecommerce_order_tracking_system.exception;

import com.example.ecommerce_order_tracking_system.common.enums.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private ExceptionResponse buildEx(HttpStatus status, ErrorCode errorCode, String message, HttpServletRequest request){
        ExceptionResponse response = new ExceptionResponse(status.value(), errorCode.name(), message);
        response.setPath(request.getRequestURI());

        return response;
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ExceptionResponse> handleBusinessEx(BusinessException ex, HttpServletRequest request){

        return ResponseEntity.badRequest().body(buildEx(HttpStatus.BAD_REQUEST, ex.getErrorCode(), ex.getMessage(), request));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationEx(MethodArgumentNotValidException ex, HttpServletRequest request){

        Map<String, String> errorMessage = new HashMap<>();

        for(FieldError fieldError : ex.getBindingResult().getFieldErrors()){
            errorMessage.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                HttpStatus.BAD_REQUEST.value(),
                ErrorCode.MALFORMED_REQUEST.name(),
                "Validation failed",
                errorMessage
        );

        exceptionResponse.setPath(request.getRequestURI());
        return ResponseEntity.badRequest().body(exceptionResponse);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNoResourceEx(NoResourceFoundException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildEx(HttpStatus.NOT_FOUND, ErrorCode.RESOURCE_NOT_FOUND,
                        "No resource for " + request.getRequestURI(), request));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ExceptionResponse> handleMethodNotSupportedEx(HttpRequestMethodNotSupportedException ex, HttpServletRequest
            request) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(buildEx(HttpStatus.METHOD_NOT_ALLOWED, ErrorCode.METHOD_NOT_ALLOWED,
                        ex.getMessage(), request));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> handleUnreadableBodyEx(HttpMessageNotReadableException ex, HttpServletRequest request) {
        return ResponseEntity.badRequest()
                .body(buildEx(HttpStatus.BAD_REQUEST, ErrorCode.MALFORMED_REQUEST,
                        "Malformed JSON request body", request));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionResponse> handleTypeMismatchEx(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        String message = "Invalid value '" + ex.getValue() + "' for parameter '" + ex.getName() + "'";
        return ResponseEntity.badRequest()
                .body(buildEx(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_PARAMETER_TYPE, message, request));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> handleRuntimeEx(RuntimeException ex, HttpServletRequest request) {
        log.error("Unhandled exception at {}", request.getRequestURI(), ex);
        return ResponseEntity.internalServerError()
                .body(buildEx(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR,
                        "An unexpected error occurred", request));
    }
}
