package com.group1.FresherAcademyManagementSystem.dtos;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Class_Score_DetailDto {
    private String traningProgram;
    private String className;
    private List<ModuleDto> modules;
}
