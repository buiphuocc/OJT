package com.group1.FresherAcademyManagementSystem.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReserveClassListDto {
    private Long id;
    private String fullName;
    private String studentId;
    private String gender;
    private Date dob;
    private ClassForReserveDto reservedClass;
    private String reason;
    private LocalDate startDate;
    private LocalDate endDate;
}
