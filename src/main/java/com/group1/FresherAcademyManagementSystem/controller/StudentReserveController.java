package com.group1.FresherAcademyManagementSystem.controller;

import com.group1.FresherAcademyManagementSystem.dtos.student_edit_dto.ReservedClassDTO;
import com.group1.FresherAcademyManagementSystem.exceptions.CustomExceptionHandler;
import com.group1.FresherAcademyManagementSystem.exceptions.CustomSuccessHandler;
import com.group1.FresherAcademyManagementSystem.services.ReserveClassService;
import com.group1.FresherAcademyManagementSystem.services.StudentEditService;
import com.group1.FresherAcademyManagementSystem.services.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/reserve_student")
@CrossOrigin("http://localhost:5173/")
public class StudentReserveController {

    private final ReserveClassService reserveClassService;

    private final StudentService studentService;

    private final CustomExceptionHandler customExceptionHandler;

    private final StudentEditService studentEditService;

//    @PatchMapping("/{classId}")
//    public ResponseEntity<Object> editStudentByReserve(@PathVariable Long classId, @RequestBody reserveClassDTO updatedStudentDetails) {
//        reserveClassDTO updatedStudent = reserveClassService.updateStudentByReserve(classId, updatedStudentDetails);
//        if (updatedStudent != null) {
//            return CustomSuccessHandler.responseBuilder("Update student successfully", HttpStatus.OK, updatedStudent);
//        } else {
//            // Assuming handleException returns a ResponseEntity<Object>
//            return CustomSuccessHandler.responseBuilder("Not found student", HttpStatus.NOT_FOUND, updatedStudent);
//        }
//    }
//
//    @PostMapping("/add")
//    public ResponseEntity<Object> addStudentReserve(@RequestBody reserveClassDTO newStudentDetails) {
//        // Gọi phương thức addStudentByReserve từ service hoặc repository
//        reserveClassDTO addedStudent = reserveClassService.addStudentByReserve(newStudentDetails);
//
//        if (addedStudent != null) {
//            return CustomSuccessHandler.responseBuilder("Update student successfully", HttpStatus.OK, addedStudent);
//        } else {
//            return CustomSuccessHandler.responseBuilder("Not found student", HttpStatus.NOT_FOUND, addedStudent);
//        }
//    }


    @Operation(
            summary = "Reassign new class for student",
            description = "Reassign new class for reserved student",
            tags = {"Reservation Student Management"})
    @RequestMapping(method = RequestMethod.PUT, path = "/reassign")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> reassignClassForStudent(
            @Parameter(description = "Student ID for reassignment")
            @RequestParam @NotNull String id,
            @Parameter(description = "Class ID for reassignment")
            @RequestParam @NotNull String classID) {
        return CustomSuccessHandler.responseBuilder("Class reassigned for student", HttpStatus.OK,
                studentEditService.addClassEntity(id, classID)
        );
    }

    @Operation(
            summary = "Terminate/ Remove status “Reservation”",
            description = "Allow the Admin to remove the record of a student's reservation",
            tags = {"Reservation Student Management"})
    @DeleteMapping("/remove")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> removeStudentInReserveList(@NotNull Long id) {
        reserveClassService.removeStudentFromReserve(id);
        return CustomSuccessHandler.responseBuilder("Remove Successfully", HttpStatus.OK,
                "Student with ID " + id + " have been removed from reservation and added to the class");
    }

    @Operation(
            summary = "Fetch recommend class for reassignment",
            description = "Give user a list of classes to reassign new class for reserved student",
            tags = {"Reservation Student Management"})
    @GetMapping("/recommend")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> fetchRecommendClass(@Parameter(required = true, description = "Class ID to fetch similar classes")
                                                 @RequestParam @NotBlank String classID,
                                                 @RequestParam @NotBlank String studentID
    ) {
        return CustomSuccessHandler.responseBuilder("Recommend class fetched", HttpStatus.OK,
                studentService.fetchRecommendClass(classID, studentID));
    }

    @Operation(
            summary = "Search reserved class with filter",
            description = "This method includes search operations by ID, clasname, " +
                    "with filters, and also allows viewing by certain criteria",
            tags = {"Reservation Student Management"})
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TRAINER')")
    public ResponseEntity<Object> searchReservedClassWithFilter(@Parameter(description = "Page number, starting from 1", required = true) @RequestParam(name = "pageNumber", defaultValue = "1") int pageNumber,
                                                                @Parameter(description = "Page size, 10 students max", required = true) @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
                                                                @Parameter(description = "Sort field default by Id", required = true) @RequestParam(name = "sortField", defaultValue = "id") String sortField,
                                                                @Parameter(description = "Sort by ascending or descending") @RequestParam(name = "sortDir", defaultValue = "asc") String sortDir,
                                                                @Parameter(description = "Search keyword") @RequestParam(value = "value", required = false) String value) {

        String[] listFilter = null;
        if (sortField != null) {
            listFilter = sortField.split("-");
        }
        return CustomSuccessHandler.responseBuilder("Search Result", HttpStatus.OK, reserveClassService.reservedClassSearchByFilter
                (pageNumber, pageSize, listFilter, sortDir, value));
    }

    @Operation(
            summary = "Set reserved class for student",
            description = "Set reserved class for student",
            tags = {"Reservation Student Management"})
    @RequestMapping(method = RequestMethod.PUT, path = "/{id}/add/reserved")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addReservedClassForStudent(
            @Parameter(description = "Student ID", required = true)
            @PathVariable @NotNull String id,
            @RequestBody @Valid ReservedClassDTO reservedClassDTO) {
        return CustomSuccessHandler.responseBuilder("Class added for student", HttpStatus.OK,
                studentEditService.addReservedClass(id, reservedClassDTO)
        );
    }
}
