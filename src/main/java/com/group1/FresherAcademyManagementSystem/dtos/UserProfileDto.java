package com.group1.FresherAcademyManagementSystem.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {
    @JsonIgnore
    private Long id;
    @NotBlank(message = "FullName cannot be blank")
    private String fullName;
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;
    private LocalDate dob;
    @NotBlank(message = "Address cannot be blank")
    private String address;
    @NotBlank(message = "Gender cannot be blank")
    private String gender;
    @NotBlank(message = "Phone number cannot be blank")
    private String phone;

}
