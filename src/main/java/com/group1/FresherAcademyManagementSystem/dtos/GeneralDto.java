package com.group1.FresherAcademyManagementSystem.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneralDto {

    private String id;
    private String fullName;
    @NotNull(message = "dob is null")
    private Date dob;
    @NotBlank(message = "Password cannot be blank")
    private String gender;
    @NotBlank(message = "phone cannot be blank")
    @Size(min = 10, max = 12, message = "phone must be between 10 - 12 digits")
    private String phone;
    @NotBlank(message = "email cannot be blank")
    @Email
    private String email;
    @NotBlank(message = "attendingStatus cannot be blank")
    private String attendingStatus;
    private String result;
    @NotBlank(message = "address cannot be blank")
    private String address;
//    @NotBlank(message = "certificationStatus cannot be blank")
    private String certificationStatus;
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "certificationDate must be in the format YYYY-MM-DD or certificationDate cannot be blank or not a Local date !")
    private String certificationDate;
    @NotBlank(message = "area cannot be blank")
    private String area;
}
