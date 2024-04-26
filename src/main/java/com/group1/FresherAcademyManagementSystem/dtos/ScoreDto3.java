package com.group1.FresherAcademyManagementSystem.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScoreDto3 {

    private Long id;
    private Float score;
    private StudentDto3 studentDto3;
}
