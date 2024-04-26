package com.group1.FresherAcademyManagementSystem.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "class")
public class ClassEntity {
    @Id
    private String id;

    @Column(name = "class_name", unique = true)
    private String className;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @CreationTimestamp
    @Column(name = "create_date")
    private LocalDate createDate;

    @UpdateTimestamp
    @Column(name = "update_date")
    private LocalDate updateDate;

    @Column(name = "duration")
    private String duration;

    @Column(name = "location")
    private String location;

    @Column(name = "status")
    private String status;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "created_by")
    private com.group1.FresherAcademyManagementSystem.models.UserEntity createdBy;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "updated_by")
    private UserEntity updatedBy;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "training_program_id")
    private TrainingProgram trainingProgram;

    @JsonIgnore
    @OneToMany(mappedBy = "classEntity", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Student_Class> student_classes = new ArrayList<>();

    @JsonBackReference
    @OneToMany(mappedBy = "classEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reserved_Class> reserved_classes = new ArrayList<>();
}
