package com.group1.FresherAcademyManagementSystem.dtos;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student_Detail_ClassDto {
    @Valid
    private GeneralDto general;
    @Valid
    private OthersDto others;
    private Class_Score_DetailDto classScoreDetail;
}
