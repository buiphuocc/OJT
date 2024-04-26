package com.group1.FresherAcademyManagementSystem.controller;

import com.group1.FresherAcademyManagementSystem.exceptions.CustomSuccessHandler;
import com.group1.FresherAcademyManagementSystem.services.StudentSheetExportService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/student/export")
public class StudentSheetExportController {

    @Autowired
    private StudentSheetExportService studentSheetExportService;

    @RequestMapping(method = RequestMethod.POST, path = "/xlsx")
    public ResponseEntity<Object> exportXLSXSheet(@RequestParam @NotNull String[] ids) throws IOException {
        Map<String, Object> file = studentSheetExportService.exportXLSSheet(ids);

        String message = "List of student exported";
        HttpStatus status_code = HttpStatus.OK;

        if (file.containsKey("invalid_id")) {
            message = "Some of student cannot be exported, ID contains invalid id";
            status_code = HttpStatus.PARTIAL_CONTENT;
            return CustomSuccessHandler.responseBuilder(message, status_code, file);
        }

        return CustomSuccessHandler.responseBuilder(message, status_code, file);

    }

    @RequestMapping(method = RequestMethod.POST, path = "/csv")
    public ResponseEntity<Object> exportCSVSheet(@RequestParam @NotNull String[] ids) throws IOException {
        Map<String, Object> file = studentSheetExportService.exportCSV(ids);

        String message = "List of student exported";
        HttpStatus status_code = HttpStatus.OK;

        if (file.containsKey("invalid_id")) {
            message = "Some of student cannot be exported, ID contains invalid id";
            status_code = HttpStatus.PARTIAL_CONTENT;
            return CustomSuccessHandler.responseBuilder(message, status_code, file);
        }

        return CustomSuccessHandler.responseBuilder(message, status_code, file);

    }
}
