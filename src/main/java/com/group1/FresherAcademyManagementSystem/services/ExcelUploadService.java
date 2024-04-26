package com.group1.FresherAcademyManagementSystem.services;

import com.group1.FresherAcademyManagementSystem.models.Student;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface ExcelUploadService {
    boolean isValidExcelFile(MultipartFile file);

    List<Student> getStudentDataFromExcel(InputStream inputStream,String classId);

//    public Object exportXLSSheet() throws IOException;

    public ResponseEntity<byte[]> exportXLSSheet() throws IOException;
}
