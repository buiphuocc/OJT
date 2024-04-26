package com.group1.FresherAcademyManagementSystem.models;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "training_program_module")
public class TrainingProgram_Module {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long programModuleID;

    @ManyToOne
    @JoinColumn(name = "program_id")
    @JsonManagedReference
    private TrainingProgram trainingProgram;

    @ManyToOne
    @JoinColumn(name = "module_id")
    @JsonManagedReference
    private Module module;
}
