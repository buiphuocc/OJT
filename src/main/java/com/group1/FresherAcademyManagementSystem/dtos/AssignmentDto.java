package com.group1.FresherAcademyManagementSystem.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.group1.FresherAcademyManagementSystem.models.Module;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignmentDto {
   @JsonIgnore
    private Long id;

    private String assignmentName;
    @JsonIgnore
    private String assignmentType;
    @JsonIgnore
    private LocalDate dueDate;
    @JsonIgnore
    private String description;
    @JsonIgnore
    private LocalDate createDate;
    @JsonIgnore
    private String createdBy;
    @JsonIgnore
    private LocalDate updateDate;
    @JsonIgnore
    private String updatedBy;
    @JsonIgnore
    private List<ScoreDto> scores;
    @JsonIgnore
    private Module module;
   private List<ScoreDto> assignmentScore;
}
