package com.group1.FresherAcademyManagementSystem.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "module")
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "module_name")
    private String moduleName;
    @CreationTimestamp
    @Column(name = "create_date")
    private LocalDate create_date;
    @Column(name = "created_by")
    private String createdBy;
    @UpdateTimestamp
    @Column(name = "update_date")
    private LocalDate updateDate;
    @Column(name = "updated_by")
    private String updatedBy;

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Student_Module> student_modules = new ArrayList<>();

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Assignment> assignments = new ArrayList<>();

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<TrainingProgram_Module> trainingProgram_modules = new ArrayList<>();
}
