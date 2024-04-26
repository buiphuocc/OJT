package com.group1.FresherAcademyManagementSystem.dtos.student_edit_dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailTemplateDto2 {
   private String id;
   private String name;
   private String description;
   private String categories;
   private String applyTo;
   private String createDate;
}
