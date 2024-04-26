package com.group1.FresherAcademyManagementSystem.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassListDto {
    private String classID;
    private String className;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
}
