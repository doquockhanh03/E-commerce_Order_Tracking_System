package com.example.ecommerce_order_tracking_system.controller;

import com.example.ecommerce_order_tracking_system.dto.request.RegisterUserDto;
import com.example.ecommerce_order_tracking_system.dto.response.UserResponseDto;
import com.example.ecommerce_order_tracking_system.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    private final AuthService usersService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody RegisterUserDto request){
        return ResponseEntity.ok(usersService.register(request));
    }

}
