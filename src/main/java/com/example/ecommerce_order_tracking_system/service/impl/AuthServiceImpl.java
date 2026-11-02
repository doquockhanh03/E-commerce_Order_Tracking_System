package com.example.ecommerce_order_tracking_system.service.impl;

import com.example.ecommerce_order_tracking_system.common.enums.ErrorCode;
import com.example.ecommerce_order_tracking_system.common.enums.UserStatus;
import com.example.ecommerce_order_tracking_system.config.ModelMapperConfig;
import com.example.ecommerce_order_tracking_system.entity.user.Roles;
import com.example.ecommerce_order_tracking_system.entity.user.UserRoles;
import com.example.ecommerce_order_tracking_system.exception.BusinessException;
import com.example.ecommerce_order_tracking_system.dto.request.RegisterUserDto;
import com.example.ecommerce_order_tracking_system.dto.response.UserResponseDto;
import com.example.ecommerce_order_tracking_system.entity.user.Users;
import com.example.ecommerce_order_tracking_system.repository.user.RolesRepo;
import com.example.ecommerce_order_tracking_system.repository.user.UserRolesRepo;
import com.example.ecommerce_order_tracking_system.repository.user.UsersRepo;
import com.example.ecommerce_order_tracking_system.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsersRepo usersRepo;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapperConfig modelMapperConfig;

    private final UserRolesRepo userRolesRepo;

    private final RolesRepo rolesRepo;

    @Override
    @Transactional
    public UserResponseDto register(RegisterUserDto request) {
        if(usersRepo.existsByEmail(request.getEmail())){
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS,
                    "Email already exists!");
        }

        if(usersRepo.existsByPhoneNumber((request.getPhoneNumber()))){
            throw new BusinessException(ErrorCode.PHONE_NUMBER_ALREADY_EXISTS,
                    "PhoneNumber already exists!");
        }

        Users users = new Users();
        users.setUsername(request.getUsername());
        users.setEmail(request.getEmail());
        users.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        users.setFirstName(request.getFirstName());
        users.setLastName(request.getLastName());
        users.setPhoneNumber(request.getPhoneNumber());
        users.setDateOfBirth(request.getDateOfBirth());
        users.setGender(request.getGender());
        users.setStatus(UserStatus.ACTIVE);
        users.setCreatedAt(Instant.now());
        users.setCreatedBy("SYSTEM");
        users.setUpdatedAt(Instant.now());
        users.setUpdatedBy("SYSTEM");
        users.setDeleted(false);

        usersRepo.save(users);

        Roles roles = rolesRepo.getByName("BUYER");
        if(roles == null){
            throw new RuntimeException("BUYER role not found!");
        }

        UserRoles userRoles = new UserRoles();
        userRoles.setUser(users);
        userRoles.setRole(roles);
        userRoles.setCreatedAt(Instant.now());
        userRoles.setCreatedBy("SYSTEM");
        userRoles.setUpdatedAt(Instant.now());
        userRoles.setUpdatedBy("SYSTEM");
        userRoles.setDeleted(false);

        userRolesRepo.save(userRoles);

        UserResponseDto userResponseDto = modelMapperConfig.modelMapper().map(users, UserResponseDto.class);
        userResponseDto.setFullName(request.getFirstName() + " " + request.getLastName());
        return userResponseDto;
    }
}
