package com.group1.FresherAcademyManagementSystem.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class StudentCreateDto {
    @NotBlank(message = "Name is mandatory")
    private String fullName;
    @NotNull(message = "Date of birth is mandatory")
    private Date dob;
    @NotBlank(message = "Gender is mandatory")
    private String gender;
    @NotBlank(message = "Phone number is mandatory")
    private String phone;
    @NotBlank(message = "Email is mandatory")
    private String email;
    @NotBlank(message = "School is mandatory")
    private String school;
    @NotBlank(message = "Major is mandatory")
    private String major;
    @NotNull(message = "Graduated date is mandatory")
    private LocalDate graduatedDate;
    @NotNull(message = "Gpa is mandatory")
    private float gpa;
    @NotBlank(message = "Address is mandatory")
    private String address;
    @NotBlank(message = "RECer is mandatory")
    private String RECer;
    @NotBlank(message = "Area is mandatory")
    private String area;
    private String classId;

    @AssertTrue(message = "Phone must be numeric")
    @JsonIgnore
    public boolean isPhoneNumeric() {
        try {
            Integer.parseInt(phone);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
