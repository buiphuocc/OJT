package com.group1.FresherAcademyManagementSystem.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "FullName cannot be blank")
    private String fullName;
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;
    private LocalDate dob;
    @NotBlank(message = "Address cannot be blank")
    private String address;
    private String gender;
    @NotBlank(message = "Phone cannot be blank")
    @Pattern(regexp = "(84|0[3|5|7|8|9])+([0-9]{8})\\b", message = "Please enter a valid(+84) phone number")
    private String phone;
    @NotBlank(message = "Username cannot be blank")
    @Pattern(regexp = "^(?=.{8,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$",
            message = "Invalid username. Please ensure it contains a number and does not include special characters")
    private String username;
    @NotEmpty(message = "Password cannot be blank")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).{8,16}$"
            , message = "Minimum 8 characters, at least one uppercase letter and number")
    private String password;
}
