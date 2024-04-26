package com.group1.FresherAcademyManagementSystem.dtos;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student_Detail_ClassDto2 {
    @Valid
    private GeneralDto general;
    @Valid
    private OthersDto others;
}