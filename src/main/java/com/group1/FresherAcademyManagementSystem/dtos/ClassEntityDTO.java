package com.group1.FresherAcademyManagementSystem.dtos;

import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ClassEntityDTO {
    private String id;
    private String className;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
}
