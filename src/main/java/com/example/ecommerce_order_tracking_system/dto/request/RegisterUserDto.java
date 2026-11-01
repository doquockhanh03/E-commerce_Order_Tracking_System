package com.example.ecommerce_order_tracking_system.dto.request;

import com.example.ecommerce_order_tracking_system.common.enums.UserGender;
import com.example.ecommerce_order_tracking_system.common.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RegisterUserDto {

    @NotBlank(message = "Username is required!")
    @Size(min = 5, message = "Username must be at least 5 characters long")
    private String username;

    @NotBlank(message = "Email is required!")
    @Email(message = "Invalid email format!")
    private String email;

    @NotBlank(message = "Password is required!")
    @Size(message = "Password must be at least 8 characters long")
    private String password;

    @NotBlank(message = "Firstname is required!")
    private String firstName;

    @NotBlank(message = "Lastname is required!")
    private String lastName;

    @NotBlank(message = "PhoneNumber is required!")
    private String phoneNumber;

    @NotNull(message = "DateOfBirth is required!")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dateOfBirth;

    @NotNull(message = "Please select your gender!")
    @Enumerated(EnumType.STRING)
    private UserGender gender;
}
