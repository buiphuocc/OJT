package com.group1.FresherAcademyManagementSystem.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "student_class")
public class Student_Class {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "attending_status")
    private String attendingStatus;

    @Column(name = "result")
    private String result;

    @Column(name = "final_score")
    private Float finalScore;

    @Column(name = "gpa_level")
    private Float gpaLevel;

    @Column(name = "certification_status")
    private String certificationStatus;

    @Column(name = "certification_date")
    private LocalDate certificationDate;

    @Column(name = "method")
    private String method;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "class_id")
    private ClassEntity classEntity;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "student_id")
    private Student student;

    public String getClassId() {
        return classEntity != null ? classEntity.getId() : null;
    }

    public String getClassName() {
        return classEntity != null ? classEntity.getClassName() : null;
    }
}
