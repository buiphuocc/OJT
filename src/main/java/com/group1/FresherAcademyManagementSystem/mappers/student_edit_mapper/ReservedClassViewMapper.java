package com.group1.FresherAcademyManagementSystem.mappers.student_edit_mapper;

import com.group1.FresherAcademyManagementSystem.dtos.student_edit_dto.ReservedClassViewDTO;
import com.group1.FresherAcademyManagementSystem.models.Student_Class;

public class ReservedClassViewMapper {
    public static ReservedClassViewDTO mapToDTO(Student_Class studentClass) {
        return new ReservedClassViewDTO(
                studentClass.getClassEntity().getId(),
                studentClass.getClassEntity().getClassName(),
                studentClass.getClassEntity().getTrainingProgram().getName()
        );
    }
}
