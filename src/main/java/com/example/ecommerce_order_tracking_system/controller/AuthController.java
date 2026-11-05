package com.example.ecommerce_order_tracking_system.controller;

import com.example.ecommerce_order_tracking_system.dto.request.RegisterUserDto;
import com.example.ecommerce_order_tracking_system.dto.response.UserResponseDto;
import com.example.ecommerce_order_tracking_system.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody RegisterUserDto request){
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(Authentication authentication){
        return ResponseEntity.ok(authService.login(authentication.getName()));
    }

}
