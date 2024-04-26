package com.group1.FresherAcademyManagementSystem.controller;


import com.group1.FresherAcademyManagementSystem.dtos.student_edit_dto.ReservedClassDTO;
import com.group1.FresherAcademyManagementSystem.dtos.student_edit_dto.StudentGeneralInfoDTO;
import com.group1.FresherAcademyManagementSystem.dtos.student_edit_dto.StudentOtherInfoDTO;
import com.group1.FresherAcademyManagementSystem.exceptions.CustomSuccessHandler;
import com.group1.FresherAcademyManagementSystem.services.StudentEditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/student")
@CrossOrigin("http://localhost:5173/")
@Validated
public class StudentEditController {

    private final StudentEditService studentEditService;

    @Autowired
    public StudentEditController(StudentEditService studentEditService) {
        this.studentEditService = studentEditService;
    }

    @Operation(
            summary = "Fetch student info for edit",
            description = "Fetch student information for edit in all student page",
            tags = {"Student Information Management"})
    @GetMapping("/recommend")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TRAINER')")
    @RequestMapping(method = RequestMethod.GET, path = "/{id}/profile")
    public ResponseEntity<?> fetchStudentInfoForEditAll(
            @Parameter(required = true, description = "Student ID")
            @PathVariable @NotBlank String id) {
        var student_profile = studentEditService.fetchStudentInfo(id);

        return CustomSuccessHandler.responseBuilder("Get student successfully",
                HttpStatus.OK, student_profile);
    }

    @Operation(
            summary = "Edit general information of student",
            description = "Edit general information of student",
            tags = {"Student Information Management"})
    @RequestMapping(method = RequestMethod.PATCH, path = "/{id}/edit/general")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> editStudentGeneral(
            @Parameter(description = "Student ID", required = true)
            @PathVariable @NotBlank String id,
            @RequestBody @Valid StudentGeneralInfoDTO studentEdit) {
        var student_general_info = studentEditService.updateStudentGeneralInfo(id, studentEdit);
        return CustomSuccessHandler.responseBuilder("Student's general info updated",
                HttpStatus.OK, student_general_info);
    }


    @Operation(
            summary = "Edit other information of student",
            description = "Edit other information of student",
            tags = {"Student Information Management"})
    @RequestMapping(method = RequestMethod.PATCH, path = "/{id}/edit/other")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> editStudentOther(
            @Parameter(description = "Student ID", required = true)
            @PathVariable @NotBlank String id,
            @RequestBody @Valid StudentOtherInfoDTO studentEdit) {
        var student_other_info = studentEditService.updateStudentOtherInfo(id, studentEdit);
        return CustomSuccessHandler.responseBuilder("Student's other info updated",
                HttpStatus.OK, student_other_info);
    }

    @Operation(
            summary = "Assign class for student",
            description = "Assign class for student",
            tags = {"Student Information Management"})
    @RequestMapping(method = RequestMethod.PUT, path = "/{id}/add/class")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addClassForStudent(
            @Parameter(description = "Student ID", required = true)
            @PathVariable @NotBlank String id,
            @Parameter(description = "Class ID", required = true)
            @RequestParam @NotBlank String classID) {
        return CustomSuccessHandler.responseBuilder("Class added for student", HttpStatus.OK,
                studentEditService.addClassEntity(id, classID)
        );
    }

    @Operation(
            summary = "Fetch enrollable classes for student",
            description = "Fetch enrollable classes for student",
            tags = {"Student Information Management"})
    @RequestMapping(method = RequestMethod.GET, path = "/{id}/fetch/enrollable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> fetchEnrollableClass(
            @Parameter(description = "Student ID", required = true)
            @PathVariable @NotBlank String id) {
        return CustomSuccessHandler.responseBuilder("Enrollable class fetched",
                HttpStatus.OK, studentEditService.fetchEnrollableClass(id));
    }

    @Operation(
            summary = "Fetch reservable class of student",
            description = "Fetch reservable class of student",
            tags = {"Student Information Management"})
    @RequestMapping(method = RequestMethod.GET, path = "/{id}/fetch/reservable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> fetchReservableClass(
            @Parameter(description = "Student ID", required = true)
            @PathVariable @NotBlank String id) {
        return CustomSuccessHandler.responseBuilder("Reservable class fetched",
                HttpStatus.OK, studentEditService.fetchReservableClass(id));
    }
}
