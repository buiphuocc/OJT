package com.group1.FresherAcademyManagementSystem.repositories;

import com.group1.FresherAcademyManagementSystem.models.TrainingProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TrainingProgramRepository extends JpaRepository<TrainingProgram, Long> {
    @Query(value = "Select * From training_program where id =:id", nativeQuery = true)
    TrainingProgram findByClass_id(Long id);


}
