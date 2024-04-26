package com.group1.FresherAcademyManagementSystem.services;

import com.group1.FresherAcademyManagementSystem.dtos.emailDTO.EmailHistoryDto;
import com.group1.FresherAcademyManagementSystem.dtos.emailDTO.EmailTemplateArrageDto;
import com.group1.FresherAcademyManagementSystem.dtos.emailDTO.EmailTemplateDetailDto;
import com.group1.FresherAcademyManagementSystem.dtos.emailDTO.EmailTemplateDto;
import com.group1.FresherAcademyManagementSystem.dtos.emailDTO.ReservationEmailPreviewDTO;
import com.group1.FresherAcademyManagementSystem.dtos.student_edit_dto.EmailTemplateDto2;
import com.group1.FresherAcademyManagementSystem.models.EmailSend;
import com.group1.FresherAcademyManagementSystem.models.EmailTemplate;
import com.group1.FresherAcademyManagementSystem.models.Student;
import com.group1.FresherAcademyManagementSystem.models.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmailService {

    void sendEmailToAStudent(Long idTemplate, String idStudent, HttpServletRequest request);

    public ReservationEmailPreviewDTO previewMail(Long idTemplate, String idStudent, HttpServletRequest request);

    void sendReservationEmailForAll(Long idTemplate, HttpServletRequest request);

    ReservationEmailPreviewDTO previewListMail(Long idTemplate, HttpServletRequest request);

    void informEmailReservation(Long idTemplate, String studentId);

    void saveHistoryEmailSend(List<EmailSend> emailSends);

    void saveHistoryEmail(UserEntity user, EmailTemplate emailTemp, String content, List<Student> students);

    void saveHistoryEmailToAStudent(UserEntity user, EmailTemplate emailTemp, String content, Student student);

    void createEmailTemplate(EmailTemplateDto emailTemplateDto, HttpServletRequest request);




    EmailTemplateDetailDto viewDetail (Long idTemplate);

    EmailTemplateDetailDto updateDetail (HttpServletRequest request ,Long idTemplate, EmailTemplateDetailDto emailTemplateDetailDto);

    Page<EmailTemplateDto2> emailByFilter(int pageNumber, int pageSize, String sortField, String sortDir, String keyword, String[] filter, String[] viewBy);

    void sendEmailRemind(String to, String subject, String text);

    List<EmailHistoryDto> getEmailSentToStudent(String idStudent);

    void sendEmailToTrainer (HttpServletRequest request, String idClass, Long idTemplate);

    ReservationEmailPreviewDTO previewMail(HttpServletRequest request, Long idTemplate, String idClass);
}
