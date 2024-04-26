package com.group1.FresherAcademyManagementSystem.dtos;

import lombok.*;

import java.time.LocalDate;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservedClassDTO {
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String classID;
    private String studentID;
}
