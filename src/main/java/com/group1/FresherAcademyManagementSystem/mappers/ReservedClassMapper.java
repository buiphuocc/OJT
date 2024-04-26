package com.group1.FresherAcademyManagementSystem.mappers;

import com.group1.FresherAcademyManagementSystem.dtos.ReservedClassDTO;
import com.group1.FresherAcademyManagementSystem.models.Reserved_Class;

public class ReservedClassMapper {
    public static ReservedClassDTO mapToDTO(Reserved_Class reservedClass) {
        return new ReservedClassDTO(
                reservedClass.getId(),
                reservedClass.getStartDate(),
                reservedClass.getEndDate(),
                reservedClass.getClassEntity().getId(),
                reservedClass.getStudent().getId()
        );
    }
}
