package com.group1.FresherAcademyManagementSystem.mappers;

import com.group1.FresherAcademyManagementSystem.dtos.ModuleDto;
import com.group1.FresherAcademyManagementSystem.models.Module;

import java.util.stream.Collectors;

public class ModuleMapper {
    public static ModuleDto mapToDto(Module module, String studentId){
        return ModuleDto.builder()
                .moduleName(module.getModuleName())
                .assignments(module.getAssignments().stream().map(item -> AssigmentMapper.mapToDto(item, studentId)).collect(Collectors.toList()))
                .build();
    }

    public static Module mapToEntity(ModuleDto moduleDto){
        return Module.builder()
                .id(moduleDto.getId())
                .moduleName(moduleDto.getModuleName())
                .assignments(moduleDto.getAssignments().stream().map(item -> AssigmentMapper.mapToEntity(item)).collect(Collectors.toList()) )
                .build();
    }
}
