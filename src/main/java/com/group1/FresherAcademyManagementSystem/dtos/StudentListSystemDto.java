package com.group1.FresherAcademyManagementSystem.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentListSystemDto {
    private String id;
    private String fullName;
    private String phone;
    private String email;
    private String gender;
    private String status;
    private Date dob;
    private String recer;
    private float gpa;
    private String major;
    private List<ClassListDto> classes;
}
