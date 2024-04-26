package com.group1.FresherAcademyManagementSystem.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentByClassDto {

    private String id;

    private String fullName;

    private String phone;

    private String email;

    private String gender;

    private String dob;

    private float gpa;

    private String major;

    private String attendingStatus;
}

