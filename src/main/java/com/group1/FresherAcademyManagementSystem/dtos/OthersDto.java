package com.group1.FresherAcademyManagementSystem.dtos;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OthersDto {
    @NotBlank(message = "school cannot be blank")
    private String school;
    @NotBlank(message = "major cannot be blank")
    private String major;
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "yearOfGraduation must be in the format YYYY-MM-DD or yearOfGraduation cannot be blank or not a Local date !")
    private String yearOfGraduation;
    @NotNull(message = "GPA must not be null")
    @DecimalMin(value = "0.0", message = "GPA cannot be less than 0.0")
    @DecimalMax(value = "10.0", message = "GPA cannot be greater than 10.0")
    private float gpa;
    @NotBlank(message = "recer cannot be blank")
    private String recer;
}
