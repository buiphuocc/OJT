package com.group1.FresherAcademyManagementSystem.dtos.emailDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailTemplateDetailDto {
    private String emailName;
    private String description;
    private String category;
    private LocalDate createdOn;
    private String createdBy;
    private Boolean useDear;
    private String content;
    private String subject;
    private String type;
}
