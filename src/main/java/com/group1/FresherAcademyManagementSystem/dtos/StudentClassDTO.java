package com.group1.FresherAcademyManagementSystem.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentClassDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String attendingStatus;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String result;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Float finalScore;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Float gpaLevel;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String certificationStatus;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate certificationDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String method;

    private String classID;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String studentID;
}
