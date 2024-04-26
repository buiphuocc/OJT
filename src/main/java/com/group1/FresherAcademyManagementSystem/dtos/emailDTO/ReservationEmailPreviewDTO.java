package com.group1.FresherAcademyManagementSystem.dtos.emailDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationEmailPreviewDTO {
    private String templateName;
    private String sender;
    private String receiver;
    private String subject;
    private String bodyText;
}
