package com.group1.FresherAcademyManagementSystem.dtos.student_edit_dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentGeneralInfoDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;
    @NotBlank
    @NotNull
    private String fullName;
    @NotBlank
    @NotNull
    private String gender;

    @NotNull
    private Date dob;

    @NotBlank
    @NotNull
    private String status;

    @NotBlank
    @NotNull
    private String phone;

    @NotBlank
    @NotNull
    @Email
    private String email;

    @NotBlank
    @NotNull
    private String address;

    @NotBlank
    @NotNull
    private String area;
}
