package com.group1.FresherAcademyManagementSystem.dtos.emailDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailTemplateDto {
    private Long id;
    private String nameTemlate;
    private String description;
    private EmailTemplateArrageDto emailTemplateArrageDto;

}
