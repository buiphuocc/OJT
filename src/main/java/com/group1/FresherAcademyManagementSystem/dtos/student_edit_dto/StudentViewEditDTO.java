package com.group1.FresherAcademyManagementSystem.dtos.student_edit_dto;

import com.group1.FresherAcademyManagementSystem.dtos.ClassEntityDTO;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentViewEditDTO {
    private StudentGeneralInfoDTO general;
    private StudentOtherInfoDTO others;
    private List<ReservedClassDTO> reservedClasses;
    private List<ClassEntityDTO> studentClasses;
}
