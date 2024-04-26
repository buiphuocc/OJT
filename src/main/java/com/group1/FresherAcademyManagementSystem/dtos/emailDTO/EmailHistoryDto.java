package com.group1.FresherAcademyManagementSystem.dtos.emailDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailHistoryDto {
    private String sendDate;
    private String modifiedBy;
    private String action;
}
