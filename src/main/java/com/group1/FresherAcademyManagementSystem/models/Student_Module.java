package com.group1.FresherAcademyManagementSystem.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "student_module")
public class Student_Module {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentModuleId;
    @Column(name = "module_score")
    private Float moduleScore;
    @Column(name = "module_level")
    private Float moduleLevel;

    @ManyToOne
    @JoinColumn(name = "student_id")
    @JsonManagedReference
    @JsonIgnore
    @JsonProperty(value = "student")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "module_id")
    @JsonManagedReference
    @JsonIgnore
    @JsonProperty(value = "module")
    private Module module;
}
