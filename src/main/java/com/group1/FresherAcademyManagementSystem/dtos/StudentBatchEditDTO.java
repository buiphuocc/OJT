package com.group1.FresherAcademyManagementSystem.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentBatchEditDTO {
    @NotNull
    @NotEmpty(message = "Input student IDs list cannot be empty.")
    private String[] studentIDs;

    @NotBlank(message = "Class ID is missing")
    private String classID;

    @NotBlank(message = "Status is missing")
    private String status;

}
