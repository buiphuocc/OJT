package com.group1.FresherAcademyManagementSystem.dtos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TrainingProgramDto {

    private Long id;


    private String name;


    private String status;


    private String code;


    private String duration;


    private LocalDate createDate;


    private LocalDate updateDate;


    private String createdBy;


    private String updatedBy;


    private List<TrainingProgram_ModuleDto> trainingProgram_modules;

    private List<ClassDto> classes = new ArrayList<>();
}
