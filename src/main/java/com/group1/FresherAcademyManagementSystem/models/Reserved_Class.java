package com.group1.FresherAcademyManagementSystem.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reserved_class")
public class Reserved_Class {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "reason_reverse")
    private String reasonReverse;
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "class_id")
    private ClassEntity classEntity;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "student_id")
    private Student student;
}
