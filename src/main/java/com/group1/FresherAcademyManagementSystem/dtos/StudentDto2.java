package com.group1.FresherAcademyManagementSystem.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDto2 {
    private String id;
    private String fullName;
    private Date dob;
    private String gender;
    private String phone;
    private String email;
    private String school;
    private String major;
    private LocalDate graduatedDate;
    @JsonIgnore
    private String FAAccount;
    @JsonIgnore
    private String status;
    @JsonIgnore
    private LocalDate joinDate;
    private float gpa;
    private String address;
    private String type;
    private String RECer;
    private String area;
    //    @JsonIgnore
//    private List<EmailSend_Student> emailSend_students = new ArrayList<>();
    @JsonIgnore
    private List<Student_ModuleDto> student_modules = new ArrayList<>();
    @JsonIgnore
    private List<ScoreDto> scores = new ArrayList<>();
    //    @JsonIgnore
//    private List<Reserved_Class> reserved_classes = new ArrayList<>();
    @JsonIgnore
    private List<Student_ClassDto> student_classes = new ArrayList<>();
}