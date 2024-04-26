package com.group1.FresherAcademyManagementSystem.dtos;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModuleDto {
    @JsonIgnore
    private Long id;
    private String moduleName;
    @JsonIgnore
    private LocalDate create_date;
    @JsonIgnore
    private String createdBy;
    @JsonIgnore
    private LocalDate updateDate;
    @JsonIgnore
    private String updatedBy;
    @JsonIgnore
    private List<Student_ModuleDto> student_modules;
    @JsonIgnore
    private List<AssignmentDto> assignmentEntitys;
    @JsonIgnore
    private List<TrainingProgram_ModuleDto> trainingProgram_modules = new ArrayList<>();
    private List<AssignmentDto> assignments;

}
