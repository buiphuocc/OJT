package com.group1.FresherAcademyManagementSystem.dtos.emailDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailTemplateArrageDto {
    private String category;
    private String type;
}
