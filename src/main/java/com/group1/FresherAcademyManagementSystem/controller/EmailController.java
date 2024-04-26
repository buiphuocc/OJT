package com.group1.FresherAcademyManagementSystem.controller;


import com.group1.FresherAcademyManagementSystem.dtos.emailDTO.EmailTemplateDetailDto;
import com.group1.FresherAcademyManagementSystem.dtos.emailDTO.EmailTemplateDto;
import com.group1.FresherAcademyManagementSystem.exceptions.CustomSuccessHandler;
import com.group1.FresherAcademyManagementSystem.services.EmailService;
import com.group1.FresherAcademyManagementSystem.services.StudentService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;

@RestController
@RequestMapping(value = "/api/v1/trainer")
@CrossOrigin("http://localhost:5173/")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    private final StudentService studentService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('TRAINER')")
    @GetMapping("/send-email-to-a-student/{idTemplate}/{idStudent}")
    public ResponseEntity<Object> sendEmail(@PathVariable("idTemplate") Long idTemplate, @PathVariable("idStudent") String idStudent, HttpServletRequest request) throws FileNotFoundException {

        emailService.sendEmailToAStudent(idTemplate, idStudent, request);

        return CustomSuccessHandler.responseBuilder("Email sent!", HttpStatus.OK, null);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('TRAINER')")
    @GetMapping("/preview-email-send-to-a-student/{idTemplate}/{idStudent}")
    public ResponseEntity<Object> previewEmail(@PathVariable("idTemplate") Long idTemplate, @PathVariable("idStudent") String idStudent, HttpServletRequest request) {

        return CustomSuccessHandler.responseBuilder("Preview email.", HttpStatus.OK, emailService.previewMail(idTemplate, idStudent, request));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('TRAINER')")
    @GetMapping("send-reservation-email-to-student/{idTemplate}")
    public ResponseEntity<Object> sendReservationEmail(@PathVariable("idTemplate") Long idTemplate, HttpServletRequest request) throws FileNotFoundException {

        emailService.sendReservationEmailForAll(idTemplate, request);

        return CustomSuccessHandler.responseBuilder("Email sent!", HttpStatus.OK, null);

    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('TRAINER')")
    @GetMapping("preview-send-all-reservation-email/{idTemplate}")
    public ResponseEntity<Object> previewSendReservationEmail(@PathVariable("idTemplate") Long idTemplate, HttpServletRequest request) {

        return CustomSuccessHandler.responseBuilder("Email sent!", HttpStatus.OK, emailService.previewListMail(idTemplate, request));

    }

    @PostMapping("/create-email-template")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TRAINER')")
    public ResponseEntity<Object> createEmailTemplate(@RequestBody EmailTemplateDto emailTemplateDto, HttpServletRequest request) {
        emailService.createEmailTemplate(emailTemplateDto, request);
        return CustomSuccessHandler.responseBuilder("Create Success !", HttpStatus.OK, "Create Success !!!");
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('TRAINER')")
    @GetMapping("view-detail-email-template/{idTemplate}")
    public ResponseEntity<Object> viewEmailTemplateDetail(@PathVariable("idTemplate") Long idTemplate) {

        return CustomSuccessHandler.responseBuilder("Email template", HttpStatus.OK, emailService.viewDetail(idTemplate));
    }

    @PutMapping("edit-email-template/{id}")
    public ResponseEntity<Object> updateEmailTemplate(@RequestParam("idTemplate") Long idTemplate,
                                                      @RequestBody EmailTemplateDetailDto emailTemplateDetailDto,
                                                      HttpServletRequest request) {
        return CustomSuccessHandler.responseBuilder("Updated", HttpStatus.OK, emailService.updateDetail(request, idTemplate, emailTemplateDetailDto));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('TRAINER')")
    @GetMapping("view-email-reservation-history/{idStudent}")
    public ResponseEntity<Object> sendMailAStudentByClass(@PathVariable("idStudent") String idStudent) {
        return CustomSuccessHandler.responseBuilder("Generated successfully", HttpStatus.OK, emailService.getEmailSentToStudent(idStudent));
    }

    @GetMapping("/search/email-template")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TRAINER')")
    public ResponseEntity<Object> searchEmailTemplateWithFilter(@Parameter(description = "Page number, starting from 1", required = true) @RequestParam(name = "pageNumber", defaultValue = "1") int pageNumber,
                                                                @Parameter(description = "Page size, 10 template max", required = true) @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
                                                                @Parameter(description = "Sort field default by Id", required = true) @RequestParam(name = "sortField", defaultValue = "id") String sortField,
                                                                @Parameter(description = "Sort by ascending or descending") @RequestParam(name = "sortDir", defaultValue = "asc") String sortDir,
                                                                @Parameter(description = "Filter by type") @RequestParam(name = "types", required = false) String types,
                                                                @Parameter(description = "View by default is all or category") @RequestParam(name = "categories", required = false) String categories,
                                                                @Parameter(description = "Search keyword") @RequestParam(value = "value", required = false, defaultValue = "") String value) {
        String[] listTypes = null;
        if (types != null) listTypes = types.split("-");
        String[] viewByCategory = null;
        if (categories != null) viewByCategory = categories.split("-");

        return CustomSuccessHandler.responseBuilder("Search Result", HttpStatus.OK, emailService.emailByFilter(pageNumber, pageSize, sortField, sortDir, value, listTypes, viewByCategory));
    }

    @GetMapping("/send-email-to-trainer-in-class/{idClass}/{idTemplate}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> sendEmailToTrainerInClass(@PathVariable("idClass") String idClass,
                                                            @PathVariable("idTemplate") Long idTemplate,
                                                            HttpServletRequest request) throws FileNotFoundException {
        emailService.sendEmailToTrainer(request, idClass, idTemplate);
        return CustomSuccessHandler.responseBuilder("Email sent!", HttpStatus.OK, null);
    }

    @GetMapping("/preview-email-send-to-trainer/{idClass}/{idTemplate}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> previewEmailToTrainerInClass(@PathVariable("idClass") String idClass,
                                                               @PathVariable("idTemplate") Long idTemplate,
                                                               HttpServletRequest request) {
        return CustomSuccessHandler.responseBuilder("Preview email.", HttpStatus.OK, emailService.previewMail(request, idTemplate, idClass));
    }

}
