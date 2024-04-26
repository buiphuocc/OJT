package com.group1.FresherAcademyManagementSystem.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student_ModuleDto {

    private Long studentModuleId;

    private float moduleScore;

    private float moduleLevel;


    private StudentDto student;


    private ModuleDto module;
}
