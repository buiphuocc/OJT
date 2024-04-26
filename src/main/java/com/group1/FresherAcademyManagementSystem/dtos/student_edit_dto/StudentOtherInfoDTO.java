package com.group1.FresherAcademyManagementSystem.dtos.student_edit_dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class StudentOtherInfoDTO {
    @NotBlank
    private String school;
    @NotBlank
    private String major;
    @NotBlank
    private String recer;
    @NotNull
    @Min(0)
    private Float gpa;
    @NotNull
    private LocalDate yearOfGraduation;
}
