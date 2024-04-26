package com.group1.FresherAcademyManagementSystem.dtos.student_edit_dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservedClassViewDTO {
    private String classID;
    private String className;
    private String programName;
}
