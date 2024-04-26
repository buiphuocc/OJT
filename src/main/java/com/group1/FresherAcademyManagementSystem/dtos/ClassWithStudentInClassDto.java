package com.group1.FresherAcademyManagementSystem.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassWithStudentInClassDto {

    private String programName;

    private String className;

    private String classStatus;

    private LocalDate startDate;

    private LocalDate endDate;

    private List<StudentByClassDto> students;

}
