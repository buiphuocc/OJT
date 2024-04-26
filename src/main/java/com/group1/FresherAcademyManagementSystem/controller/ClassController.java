package com.group1.FresherAcademyManagementSystem.controller;

import com.group1.FresherAcademyManagementSystem.exceptions.CustomSuccessHandler;
import com.group1.FresherAcademyManagementSystem.services.ClassService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/class")
@CrossOrigin("http://localhost:5173/")
public class ClassController {

    private final ClassService classService;

    @Autowired
    public ClassController(ClassService classService) {
        this.classService = classService;
    }

    @Operation(
            summary = "Show list of all classes",
            description = "This method show the list of all classes and can be use by admin/trainer user",
            tags = {"Student Information Management"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('TRAINER')")
    @GetMapping("/classes")
    public ResponseEntity<Object> getAllClass() {
        return CustomSuccessHandler.responseBuilder("Generate classes list successfully", HttpStatus.OK, classService.getAllClass());
    }

    @Operation(
            summary = "Search class with filter",
            description = "This method includes search operations by ID, clasname, " +
                    "with filters, and also allows viewing by certain criteria",
            tags = {"Student Information Management"})
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TRAINER')")
    public ResponseEntity<Object> searchClassWithFilter(@Parameter(description = "Page number, starting from 1", required = true) @RequestParam(name = "pageNumber", defaultValue = "1") int pageNumber,
                                                        @Parameter(description = "Page size, 10 students max", required = true) @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
                                                        @Parameter(description = "Sort field default by Id", required = true) @RequestParam(name = "sortField", defaultValue = "id") String sortField,
                                                        @Parameter(description = "Sort by ascending or descending") @RequestParam(name = "sortDir", defaultValue = "asc") String sortDir,
                                                        @Parameter(description = "View by default is planning or inprogress/finished") @RequestParam(name = "viewBy", required = false) String viewBy,
                                                        @Parameter(description = "Search keyword") @RequestParam(value = "value", required = false) String value) {

        String[] viewByStatus = null;
        if (viewBy != null) viewByStatus = viewBy.split("-");

        return CustomSuccessHandler.responseBuilder("Search Result", HttpStatus.OK, classService.getClassSearchByFilter
                (pageNumber, pageSize, sortField, sortDir, value, viewByStatus));
    }
}
