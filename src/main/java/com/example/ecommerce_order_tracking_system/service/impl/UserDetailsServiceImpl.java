package com.example.ecommerce_order_tracking_system.service.impl;

import com.example.ecommerce_order_tracking_system.common.enums.UserStatus;
import com.example.ecommerce_order_tracking_system.entity.user.Users;
import com.example.ecommerce_order_tracking_system.repository.user.UsersRepo;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@NullMarked
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsersRepo usersRepo;


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {

        Users user;
        if(identifier.contains("@")){
            Optional<Users> users = usersRepo.findByEmail(identifier);

            if(users.isEmpty()){
                throw new RuntimeException("user not found!");
            }
            user = users.get();
        }else if (identifier.matches("\\d+")){
            Optional<Users> users = usersRepo.findByPhoneNumber(identifier);

            if(users.isEmpty()){
                throw new RuntimeException("user not found!");
            }
            user = users.get();
        }
        else{
            Optional<Users> users = usersRepo.findByUsername(identifier);

            if(users.isEmpty()){
                throw new RuntimeException("user not found!");
            }
            user = users.get();
        }


        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPasswordHash())
                .authorities(user.getUserRoles().stream()
                        .filter(userRole -> !userRole.getDeleted())
                        .map(userRole -> "ROLE_" + userRole.getRole().getName())
                        .toArray(String[]::new))
                .disabled(user.getStatus() != UserStatus.ACTIVE)
                .build();
    }
}

