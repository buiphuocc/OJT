package com.group1.FresherAcademyManagementSystem.mappers;

import com.group1.FresherAcademyManagementSystem.dtos.Student_ModuleDto;
import com.group1.FresherAcademyManagementSystem.models.Student_Module;

public class StudentModuleMapper {

    public static Student_ModuleDto mapToDto(Student_Module student_module) {
        return Student_ModuleDto.builder()
                .build();
    }

}
