package com.group1.FresherAcademyManagementSystem.controller;


import com.group1.FresherAcademyManagementSystem.dtos.*;
import com.group1.FresherAcademyManagementSystem.dtos.student_edit_dto.ReservedClassDTO;
import com.group1.FresherAcademyManagementSystem.exceptions.CustomSuccessHandler;
import com.group1.FresherAcademyManagementSystem.models.ImportOption;
import com.group1.FresherAcademyManagementSystem.services.ClassService;
import com.group1.FresherAcademyManagementSystem.services.StudentEditService;
import com.group1.FresherAcademyManagementSystem.services.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/student")
@CrossOrigin("http://localhost:5173/")
public class StudentController {
    private final StudentService studentService;

    private final ClassService classService;

    private final StudentEditService studentEditService;

    @Operation(
            summary = "Show list of all students in system",
            description = "Generate a list of all students currently have in the system"
    )
    @PreAuthorize("hasRole('ADMIN') or hasRole('TRAINER')")
    @GetMapping("/getAll")
    public ResponseEntity<Object> getAllStudent() {
        return CustomSuccessHandler.responseBuilder("Generate student list successfully", HttpStatus.OK, studentService.getAllStudent());
    }

    @Operation(
            summary = "Show list of all students with status Reservation",
            description = "Generate a list of all students with status Reservation currently have in the system"
    )
    @PreAuthorize("hasRole('ADMIN') or hasRole('TRAINER')")
    @GetMapping("/getAllWithReservation")
    public ResponseEntity<Object> getAllStudentWithReservation() {
        return CustomSuccessHandler.responseBuilder("Generate student list successfully", HttpStatus.OK, studentService.getAllStudentWithReservation());
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('TRAINER')")
    @PutMapping("/students/{classId}/{studentId}")
    public ResponseEntity<?> updateInfoStudent(@PathVariable("classId") String classId, @PathVariable("studentId") String studentId, @RequestBody @Valid Student_Detail_ClassDto2 studentDetailClassDto2) {
        studentService.updateInfoStudentInClass(classId, studentId, studentDetailClassDto2);
        return CustomSuccessHandler.responseBuilder("update successfully !", HttpStatus.OK, "");
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('TRAINER')")
    @GetMapping("/students/{classId}/{studentId}")
    public ResponseEntity<Object> getStudentDetail(@PathVariable("classId") String classId, @PathVariable("studentId") String studentId) {
        Student_Detail_ClassDto studentResponse = studentService.getStudent_detail(classId, studentId);
        return CustomSuccessHandler.responseBuilder("get successfully !", HttpStatus.OK, studentResponse);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('TRAINER')")
    @PutMapping("/student/edit/status/{classId}/{studentId}")
    public ResponseEntity<Object> editStatusStudentClass(@PathVariable("classId") String classId, @PathVariable("studentId") String studentId, @RequestParam("status") String status) {
        studentService.updateStatusStudentClass(classId, studentId, status);
        return CustomSuccessHandler.responseBuilder("view student detail successfully !", HttpStatus.OK, null);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('TRAINER')")
    @PutMapping("/{studentId}/{classId}")
    public ResponseEntity<Object> updateCertificateStatus(@PathVariable("studentId") String studentId,
                                                          @PathVariable("classId") String classId,
                                                          @RequestParam("status") String status,
                                                          @RequestParam("certificateDate") LocalDate certificateDate) {
        studentService.updateCertificateStatusStudentClass(studentId, classId, status, certificateDate);
        return CustomSuccessHandler.responseBuilder("Update Certificate Status and Date successfully !", HttpStatus.OK, null);

    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('TRAINER')")
    @PostMapping("/upload-students-data/{classId}")
    public ResponseEntity<?> uploadStudentsData(@RequestParam("file") MultipartFile file,
                                                @RequestParam("importOption") ImportOption importOption,
                                                @PathVariable("classId") String classId) {
        try {
            this.studentService.saveStudentsToDatabase(file, importOption, classId);
            return CustomSuccessHandler.responseBuilder("EM4 The File import process has been completed.", HttpStatus.OK, null);
        } catch (UnsupportedOperationException e) {
            return CustomSuccessHandler.responseBuilder(
                    e.getMessage(),
                    HttpStatus.BAD_REQUEST,
                    null
            );
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('TRAINER')")
    @RequestMapping(method = RequestMethod.PUT, path = "/add/reserved")
    public ResponseEntity<?> addReservedClassForStudent(@RequestParam @NotNull String id,
                                                        @RequestBody ReservedClassDTO reservedClassDTO) {
        return CustomSuccessHandler.responseBuilder("Class added for student", HttpStatus.OK,
                studentEditService.addReservedClassFromList(id, reservedClassDTO)
        );
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('TRAINER')")
    @GetMapping("/{studentId}")
    public ResponseEntity<Object> getStudentById(@PathVariable String studentId) {

        StudentDto student = studentService.getStudentById(studentId);
        if (student != null) {
            return CustomSuccessHandler.responseBuilder("Find student successfully", HttpStatus.OK, student);
        } else {
            // Assuming handleException returns a ResponseEntity<Object>
            return CustomSuccessHandler.responseBuilder("Not found student", HttpStatus.NOT_FOUND, student);
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('TRAINER')")
    @PatchMapping("/{studentId}")
    public ResponseEntity<Object> editStudentByReserve(@PathVariable String studentId, @RequestBody StudentDto updatedStudentDetails) {
        StudentDto updatedStudent = studentService.editStudentbyReserve(studentId, updatedStudentDetails);
        if (updatedStudent != null) {
            return CustomSuccessHandler.responseBuilder("Edit student successfully", HttpStatus.OK, updatedStudent);
        } else {
            // Assuming handleException returns a ResponseEntity<Object>
            return CustomSuccessHandler.responseBuilder("Not found student", HttpStatus.NOT_FOUND, updatedStudent);
        }
    }


    @DeleteMapping("/{studentId}")
    public ResponseEntity<Object> disableStudentById(@PathVariable String studentId) {
        try {
            // Gọi phương thức disable của StudentService
            studentService.deleteStudentById(studentId);

            return CustomSuccessHandler.responseBuilder(
                    "Delete Student Successfully",
                    HttpStatus.OK,
                    studentId
            );
        } catch (EntityNotFoundException e) {
            return CustomSuccessHandler.responseBuilder(
                    "Student not found with ID: " + studentId,
                    HttpStatus.NOT_FOUND,
                    null
            );
        } catch (UnsupportedOperationException e) {
            return CustomSuccessHandler.responseBuilder(
                    e.getMessage(),
                    HttpStatus.BAD_REQUEST,
                    null
            );
        }
    }

    @DeleteMapping("/{studentId}/{classId}")
    public ResponseEntity<Object> disableStudentByIdInClass(@PathVariable String studentId,
                                                            @PathVariable String classId) {
        try {
            // Gọi phương thức disable của StudentService
            studentService.deleteStudentByIdInClass(studentId, classId);

            return CustomSuccessHandler.responseBuilder(
                    "Delete Student Successfully",
                    HttpStatus.OK,
                    studentId
            );
        } catch (EntityNotFoundException e) {
            return CustomSuccessHandler.responseBuilder(
                    "Student not found with ID: " + studentId,
                    HttpStatus.NOT_FOUND,
                    null
            );
        } catch (UnsupportedOperationException e) {
            return CustomSuccessHandler.responseBuilder(
                    e.getMessage(),
                    HttpStatus.BAD_REQUEST,
                    null
            );
        }
    }

    @Operation(
            summary = "Edit status of selected student",
            description = "Edit status of selected student in class",
            tags = {"Student Information Management"})
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.PATCH, path = "/update-status/batch")
    public ResponseEntity<Object> updateSelectedStudents(@Valid @RequestBody StudentBatchEditDTO batch) {
        var response_object = studentService.updateSelectedStudentStatus(batch.getStudentIDs()
                , batch.getClassID(), batch.getStatus().toLowerCase());

        boolean contain_failed = response_object.containsKey("failed");
        boolean contain_success = response_object.containsKey("success");

        String message = "Unknown Status";
        HttpStatus status_code = HttpStatus.FORBIDDEN;

        if (contain_failed && contain_success) {
            message = "Some of student cannot be updated, ID contains invalid id";
            status_code = HttpStatus.PARTIAL_CONTENT;
            return CustomSuccessHandler.responseBuilder(message, status_code, response_object);
        }

        if (contain_failed) {
            message = "Student batch failed to update, All IDs are invalid";
            status_code = HttpStatus.PRECONDITION_FAILED;
        }

        if (contain_success) {
            message = "Student batch updated";
            status_code = HttpStatus.OK;
        }

        return CustomSuccessHandler.responseBuilder(message, status_code, response_object);
    }


    @GetMapping("/students-by-class/{classId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> getStudentByClass(@PathVariable String classId) {

        return CustomSuccessHandler.responseBuilder("Generate student list successfully", HttpStatus.OK, classService.findClassAndStudentById(classId));
    }

    @Operation(
            summary = "Add a new student to the system",
            description = "Add a new student to the system. If add from class then the classId is mandatory. If add from the system then classId can be null"
    )
    @PostMapping("/create")
    public ResponseEntity<Object> createStudent(@RequestBody @Valid StudentCreateDto studentCreateDto) {
        return CustomSuccessHandler.responseBuilder("Created successfully", HttpStatus.CREATED, studentService.createStudent(studentCreateDto));

    }

    @Operation(
            summary = "Search student with filter",
            description = "This method includes search operations by ID, fullname, or email with filters, and also allows viewing by certain criteria",
            tags = {"Student Information Management"})
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TRAINER')")
    public ResponseEntity<Object> searchStudentWithFilter(@Parameter(description = "Page number, starting from 1", required = true) @RequestParam(name = "pageNumber", defaultValue = "1") int pageNumber,
                                                          @Parameter(description = "Page size, 10 students max", required = true) @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
                                                          @Parameter(description = "Sort field default by Id", required = true) @RequestParam(name = "sortField", defaultValue = "id") String sortField,
                                                          @Parameter(description = "Sort by ascending or descending") @RequestParam(name = "sortDir", defaultValue = "asc") String sortDir,
                                                          @Parameter(description = "Filter by fullname/email/phone") @RequestParam(name = "filter", required = false) String filter,
                                                          @Parameter(description = "View by default is active or inactive/disable") @RequestParam(name = "viewBy", required = false) String viewBy,
                                                          @Parameter(description = "Search keyword") @RequestParam(value = "value", required = false) String value) {
        String[] listFilter = null;
        if (filter != null) listFilter = filter.split("-");
        String[] viewByStatus = null;
        if (viewBy != null) viewByStatus = viewBy.split("-");

        if (pageNumber == 0 && pageSize == 0) {
            return CustomSuccessHandler.responseBuilder("Search Result", HttpStatus.OK, studentService.getAllStudentsByFilter(
                    value, listFilter, viewByStatus));
        }

        return CustomSuccessHandler.responseBuilder("Search Result", HttpStatus.OK, studentService.getStudentSearchByFilter
                (pageNumber, pageSize, sortField, sortDir, value, listFilter, viewByStatus));
    }

    @Operation(
            summary = "Search student in class with filter",
            description = "This method includes search operations by ID, fullname, or email with filters, and also allows viewing by certain criteria in that specific class",
            tags = {"Student Information Management"})
    @GetMapping("/{classId}/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TRAINER')")
    public ResponseEntity<Object> searchStudentInClassWithFilter(@PathVariable String classId,
                                                                 @Parameter(description = "Page number, starting from 1", required = true) @RequestParam(name = "pageNumber", defaultValue = "1") int pageNumber,
                                                                 @Parameter(description = "Page size, 10 students max", required = true) @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
                                                                 @Parameter(description = "Sort field default by Id", required = true) @RequestParam(name = "sortField", defaultValue = "id") String sortField,
                                                                 @Parameter(description = "Sort by ascending or descending") @RequestParam(name = "sortDir", defaultValue = "asc") String sortDir,
                                                                 @Parameter(description = "Filter by fullname/email/phone") @RequestParam(name = "filter", required = false) String filter,
                                                                 @Parameter(description = "View by default is inclass or dropout") @RequestParam(name = "viewBy", required = false) String viewBy,
                                                                 @Parameter(description = "Search keyword") @RequestParam(value = "value", required = false) String value) {
        String[] listFilter = null;
        if (filter != null) listFilter = filter.split("-");
        String[] viewByStatus = null;
        if (viewBy != null) viewByStatus = viewBy.split("-");


        if (pageNumber == 0 && pageSize == 0) {
            return CustomSuccessHandler.responseBuilder("Search Result", HttpStatus.OK, studentService.getAllStudentsInClassByFilter(
                    value, listFilter, viewByStatus,classId));
        }

        return CustomSuccessHandler.responseBuilder("Search Result", HttpStatus.OK, studentService.searchStudentInClassSearchByFilter
                (pageNumber, pageSize, sortField, sortDir, value, listFilter, viewByStatus, classId));
    }

    @Operation(
            summary = "Show Student's reservation details",
            description = "Show the details of student's reservation which contains list of reserved classes of that student "
    )
    @GetMapping("/show-reservation-detail")
    public ResponseEntity<Object> showReservationDetail(@RequestParam("reservedId") Long reservedId) {
        return CustomSuccessHandler.responseBuilder("Reservation Details", HttpStatus.OK, studentService.getStudentReserveDetail(reservedId));

    }


}
