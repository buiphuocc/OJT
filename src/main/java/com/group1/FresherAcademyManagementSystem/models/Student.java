package com.group1.FresherAcademyManagementSystem.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "student")
public class Student {
    @Id
    @Column(unique = true)
    private String id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "dob")
    private Date dob;

    @Column(name = "gender")
    private String gender;

    @Column(name = "phone", unique = true)
    private String phone;

    @Column(name = "email", unique = true)
    private String email;

    @JsonIgnore
    @Column(name = "school")
    private String school;

    @Column(name = "major")
    private String major;

    @JsonIgnore
    @Column(name = "graduated_date")
    private LocalDate graduatedDate;

    @Column(name = "gpa")
    private Float gpa;

    @JsonIgnore
    @Column(name = "address")
    private String address;

    @JsonIgnore
    @Column(name = "FA_account")
    private String FAAccount;

    @JsonIgnore
    @Column(name = "type")
    private String type;

    @Column(name = "status")
    private String status;

    @Column(name = "recer")
    private String RECer;

    @JsonIgnore
    @CreationTimestamp
    @Column(name = "join_date")
    private LocalDate joinDate;

    @JsonIgnore
    @Column(name = "area")
    private String area;

    @JsonIgnore
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EmailSend_Student> emailSend_students = new ArrayList<>();


    @JsonIgnore
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Student_Module> student_modules = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Score> scores = new ArrayList<>();

    @JsonBackReference
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reserved_Class> reserved_classes = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Student_Class> studentClasses = new ArrayList<>();
}
