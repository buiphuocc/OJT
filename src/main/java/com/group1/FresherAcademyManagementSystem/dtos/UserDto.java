//package com.group1.FresherAcademyManagementSystem.dto;
//
//import com.group1.FresherAcademyManagementSystem.models.ClassEntity;
//import com.group1.FresherAcademyManagementSystem.models.EmailSend;
//import com.group1.FresherAcademyManagementSystem.models.EmailTemplate;
//import jakarta.persistence.*;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//public class UserDto {
//
//    private Long id;
//
//    private String fullName;
//
//    private String email;
//
//    private Date dob;
//
//    private String address;
//
//    private String gender;
//
//    private String phone;
//
//    private String username;
//
//    private String password;
//
//    private String role;
//
//
//    private List<EmailSendDto> emailSends;
//
//    @OneToMany(mappedBy = "userCreate", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<EmailTemplate> emailTemplatesUserCreate  = new ArrayList<>();
//
//    @OneToMany(mappedBy = "userUpdate", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<EmailTemplate> emailTemplatesUserUpdate  = new ArrayList<>();
//
//    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<ClassEntity> classUserCreate  = new ArrayList<>();
//
//    @OneToMany(mappedBy = "updatedBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<ClassEntity> classUserUpdate  = new ArrayList<>();
//}
