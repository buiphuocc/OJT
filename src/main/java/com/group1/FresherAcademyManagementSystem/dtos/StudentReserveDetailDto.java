package com.group1.FresherAcademyManagementSystem.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentReserveDetailDto {
    private String trainingProgram; //training program name
    private LocalDate startDate; //of class
    private LocalDate endDate; // of class
    private String classCode;
    private String className;
    private String studentId;
    private String fullName;
    private String email;
    private String phone;
    private LocalDate reserveStartDate;
    private LocalDate reserveEndDate;
    private String reason;
}
