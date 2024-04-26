package com.group1.FresherAcademyManagementSystem.dtos;

import java.time.LocalDate;

public class Student_ClassDto {

    private Long id;

    private String attendingStatus;

    private String result;

    private Float finalScore;

    private Float gpaLevel;

    private String certificationStatus;

    private LocalDate certificationDate;

    private String method;

    private ClassDto classEntity;

    private StudentDto student;
}
