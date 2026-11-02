package com.example.ecommerce_order_tracking_system.dto.response;

import com.example.ecommerce_order_tracking_system.entity.user.UserRoles;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {
    String username;

    String fullName;

    String phoneNumber;

    String Email;

    String dateOfBirth;

    String gender;
}
