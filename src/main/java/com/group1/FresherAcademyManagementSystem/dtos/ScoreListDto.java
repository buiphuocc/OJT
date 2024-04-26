package com.group1.FresherAcademyManagementSystem.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScoreListDto {
    private String studentId;
    private String fullName;
    private Long id;
    private Float score;

}
