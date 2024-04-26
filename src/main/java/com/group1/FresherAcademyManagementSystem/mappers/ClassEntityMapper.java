package com.group1.FresherAcademyManagementSystem.mappers;

import com.group1.FresherAcademyManagementSystem.dtos.ClassEntityDTO;
import com.group1.FresherAcademyManagementSystem.dtos.ClassForReserveDto;
import com.group1.FresherAcademyManagementSystem.dtos.ClassListDto;
import com.group1.FresherAcademyManagementSystem.models.ClassEntity;
import com.group1.FresherAcademyManagementSystem.models.Student_Class;

public class ClassEntityMapper {
    public static ClassEntityDTO mapToDTO(ClassEntity classEntity) {
        return new ClassEntityDTO(
                classEntity.getId(),
                classEntity.getClassName(),
                classEntity.getStartDate(),
                classEntity.getEndDate(),
                classEntity.getStatus()
        );
    }


    public static ClassEntityDTO mapToClassDTO(Student_Class studentClass) {
        return new ClassEntityDTO(
                studentClass.getClassEntity().getId(),
                studentClass.getClassEntity().getClassName(),
                studentClass.getClassEntity().getStartDate(),
                studentClass.getClassEntity().getEndDate(),
                studentClass.getAttendingStatus()
        );
    }

    public static ClassListDto mapToClassListDto(ClassEntity classEntity){
        return new ClassListDto(
                classEntity.getId(),
                classEntity.getClassName(),
                classEntity.getStartDate(),
                classEntity.getEndDate(),
                classEntity.getStatus()
        );
    }

    public static ClassForReserveDto mapToReserveClassDto(ClassEntity classEntity){
        return new ClassForReserveDto(
                classEntity.getId(),
                classEntity.getClassName()
        );
    }
}
