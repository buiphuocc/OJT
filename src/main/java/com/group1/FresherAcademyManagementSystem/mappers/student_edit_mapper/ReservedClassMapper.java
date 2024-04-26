package com.group1.FresherAcademyManagementSystem.mappers.student_edit_mapper;

import com.group1.FresherAcademyManagementSystem.dtos.student_edit_dto.ReservedClassDTO;
import com.group1.FresherAcademyManagementSystem.models.ClassEntity;
import com.group1.FresherAcademyManagementSystem.models.Reserved_Class;
import com.group1.FresherAcademyManagementSystem.models.Student;

public class ReservedClassMapper {
    public static Reserved_Class mapToEntity(ReservedClassDTO reservedClass, ClassEntity classEntity, Student student) {
        return new Reserved_Class(
                null,
                reservedClass.getStartDate(),
                reservedClass.getEndDate(),
                reservedClass.getReasonReverse(),
                classEntity,
                student
        );
    }

    public static ReservedClassDTO mapToDTO(Reserved_Class reservedClass) {
        return new ReservedClassDTO(reservedClass.getId(),
                reservedClass.getStartDate(),
                reservedClass.getEndDate(),
                reservedClass.getReasonReverse(),
                reservedClass.getClassEntity().getId(),
                reservedClass.getClassEntity().getClassName());
    }
}
