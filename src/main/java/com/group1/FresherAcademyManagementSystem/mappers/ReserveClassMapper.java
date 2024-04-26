package com.group1.FresherAcademyManagementSystem.mappers;

import com.group1.FresherAcademyManagementSystem.dtos.reserveClassDTO;
import com.group1.FresherAcademyManagementSystem.models.Reserved_Class;

public class ReserveClassMapper {
    public static reserveClassDTO mapToDto(Reserved_Class reservedClass) {
        return new reserveClassDTO(
                reservedClass.getId(),
                reservedClass.getEndDate(),
                reservedClass.getStartDate(),
                reservedClass.getClassEntity().getId(),
                reservedClass.getStudent().getId(),
                reservedClass.getReasonReverse()
        );
    }
}
