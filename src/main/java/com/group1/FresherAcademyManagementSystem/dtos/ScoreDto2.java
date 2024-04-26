package com.group1.FresherAcademyManagementSystem.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScoreDto2 {

    private Long id;
    private Float score;
    @JsonIgnore
    private String scoreName;

    @JsonIgnore
    private LocalDate submissionDate;

    @JsonIgnore
    private AssignmentDto assignment;

    @JsonIgnore
    private StudentDto student;

}
