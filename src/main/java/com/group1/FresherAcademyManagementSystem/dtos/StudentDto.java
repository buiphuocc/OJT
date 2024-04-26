package com.group1.FresherAcademyManagementSystem.dtos;

import com.group1.FresherAcademyManagementSystem.models.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDto {
    private String id;
    private String fullName;
    private Date dob;
    private String gender;
    private String phone;
    private String email;
    private String school;
    private String major;
    private LocalDate graduatedDate;
    private float gpa;
    private String address;
    private String FAAccount;
    private String type;
    private String status;
    private String RECer;
    private LocalDate joinDate;
    private String area;
    private List<EmailSend_Student> emailSend_students;
    private List<Student_Module> student_modules;
    private List<Score> scores;
    private List<Reserved_Class> reserved_classes;
    private List<Student_Class> student_classes;
}
