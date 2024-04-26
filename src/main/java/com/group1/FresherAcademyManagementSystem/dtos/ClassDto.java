package com.group1.FresherAcademyManagementSystem.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.group1.FresherAcademyManagementSystem.models.Reserved_Class;
import com.group1.FresherAcademyManagementSystem.models.Student_Class;
import com.group1.FresherAcademyManagementSystem.models.TrainingProgram;
import com.group1.FresherAcademyManagementSystem.models.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassDto {

    private String classId;


    private String className;


    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDate createDate;

    private LocalDate updateDate;

    private String duration;

    private String location;

    private String status;

    private UserEntity createdBy;

    private UserEntity updatedBy;

    private TrainingProgram trainingProgram;

    private List<Student_Class> student_classes = new ArrayList<>();

    private List<Reserved_Class> reserved_classes = new ArrayList<>();

}
