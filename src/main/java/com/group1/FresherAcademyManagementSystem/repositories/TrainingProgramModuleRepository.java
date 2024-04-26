package com.group1.FresherAcademyManagementSystem.repositories;

import com.group1.FresherAcademyManagementSystem.models.TrainingProgram_Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TrainingProgramModuleRepository extends JpaRepository<TrainingProgram_Module, Long> {
    @Query(value = "Select * from training_program_module where program_id = :programId", nativeQuery = true)
    List<TrainingProgram_Module> findAllByProgramId(Long programId);
}