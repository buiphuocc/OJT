package com.group1.FresherAcademyManagementSystem.controller;


import com.group1.FresherAcademyManagementSystem.services.ExcelUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/student/import")
@CrossOrigin("http://localhost:5173/")
public class StudentSheetImportController {
    private final ExcelUploadService excelUploadService;

    @Autowired
    public StudentSheetImportController(ExcelUploadService excelUploadService) {
        this.excelUploadService = excelUploadService;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/xlsx")
    public ResponseEntity<byte[]> exportXLSXSheet() throws IOException {
        return excelUploadService.exportXLSSheet();
    }
}
