package com.group1.FresherAcademyManagementSystem.services.Impl;

import com.group1.FresherAcademyManagementSystem.dtos.StudentSheetDTO;
import com.group1.FresherAcademyManagementSystem.mappers.StudentSheetMapper;
import com.group1.FresherAcademyManagementSystem.repositories.StudentRepository;
import com.group1.FresherAcademyManagementSystem.services.StudentSheetExportService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FastByteArrayOutputStream;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;

@Service
public class StudentSheetExportServiceImpl implements StudentSheetExportService {

    @Autowired
    private StudentRepository studentRepository;


    @Override
    public Map<String, Object> exportXLSSheet(String[] ids) throws IOException {
        Map<String, Object> response_object = new HashMap<>();
        ArrayList<String> invalid = new ArrayList<>();
        FastByteArrayOutputStream fstream = new FastByteArrayOutputStream();
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Student list");

        var title_row = sheet.createRow(0);
        title_row.createCell(0).setCellValue("Full name");
        title_row.createCell(1).setCellValue("Date of birth");
        title_row.createCell(2).setCellValue("Mail");
        title_row.createCell(3).setCellValue("Phone");
        title_row.createCell(4).setCellValue("GPA");
        title_row.createCell(5).setCellValue("RECer");
        title_row.createCell(6).setCellValue("Gender");
        title_row.createCell(7).setCellValue("Graduated Date");
        title_row.createCell(8).setCellValue("Address");
        title_row.createCell(9).setCellValue("Area");
        title_row.createCell(10).setCellValue("FA Account");
        title_row.createCell(11).setCellValue("Major");
        title_row.createCell(12).setCellValue("School");
        title_row.createCell(13).setCellValue("Status");
        title_row.createCell(14).setCellValue("Type");

        for (int row = 1; row < ids.length + 1; row++) {
            String id = ids[row - 1];

            var curRow = sheet.createRow(row);

            curRow.createCell(0).setCellValue(id);

            var current_student = studentRepository.findById(id);

            if (!current_student.isPresent()) {
                curRow.createCell(0).setCellValue("NOT FOUND");
                invalid.add(id);
                continue;
            }

            var stdData = current_student.get();
            curRow.createCell(0).setCellValue(stdData.getFullName());
            curRow.createCell(1).setCellValue(stdData.getDob().toString());
            curRow.createCell(2).setCellValue(stdData.getEmail());
            curRow.createCell(3).setCellValue(stdData.getPhone());
            curRow.createCell(4).setCellValue(stdData.getGpa());
            curRow.createCell(5).setCellValue(stdData.getRECer());
            curRow.createCell(6).setCellValue(stdData.getGender());
            curRow.createCell(7).setCellValue(stdData.getGraduatedDate());
            curRow.createCell(8).setCellValue(stdData.getAddress());
            curRow.createCell(9).setCellValue(stdData.getArea());
            curRow.createCell(10).setCellValue(stdData.getFAAccount());
            curRow.createCell(11).setCellValue(stdData.getMajor());
            curRow.createCell(12).setCellValue(stdData.getSchool());
            curRow.createCell(13).setCellValue(stdData.getStatus());
            curRow.createCell(14).setCellValue(stdData.getType());
        }

        workbook.write(fstream);
        workbook.close();
        fstream.close();

        response_object.put("sheet_file", fstream.toByteArray());

        if (!invalid.isEmpty())
            response_object.put("invalid_id", invalid);

        return response_object;
    }

    @Override
    public Map<String, Object> exportCSV(String[] ids) throws IOException {
        String[] HEADERS = {
                "Full name", "Date of birth", "Mail", "Phone", "GPA", "RECer", "Gender", "Graduated Date", "Address",
                "Area", "FA Account", "Major", "School", "Status", "Type"
        };
        Map<String, Object> response_object = new HashMap<>();

        ArrayList<String> invalid = new ArrayList<>();
        FastByteArrayOutputStream fstream = new FastByteArrayOutputStream();
        CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(fstream),
                CSVFormat.DEFAULT.builder().setHeader(HEADERS).build());


        for (int row = 1; row < ids.length + 1; row++) {
            String id = ids[row - 1];
            var current_student = studentRepository.findById(id);

            if (!current_student.isPresent()) {
                invalid.add(id);
                continue;
            }

            var stdData = current_student.get();

            csvPrinter.printRecord(
                    stdData.getFullName(),
                    stdData.getDob().toString(),
                    stdData.getEmail(),
                    stdData.getPhone(),
                    stdData.getGpa(),
                    stdData.getRECer(),
                    stdData.getGender(),
                    stdData.getGraduatedDate(),
                    stdData.getAddress(),
                    stdData.getArea(),
                    stdData.getFAAccount(),
                    stdData.getMajor(),
                    stdData.getSchool(),
                    stdData.getStatus(),
                    stdData.getType()
            );
        }

        csvPrinter.flush();
        fstream.close();
        response_object.put("sheet_file", fstream.toByteArray());

        if (!invalid.isEmpty())
            response_object.put("invalid_id", invalid);

        return response_object;
    }

    @Override
    public List<StudentSheetDTO> exportDataSheet(String[] ids) {
        List<StudentSheetDTO> studentList = new ArrayList<>();

        if (ids.length == 0){
            return studentRepository.findAll()
                    .stream()
                    .map(StudentSheetMapper::mapToDTO)
                    .toList();
        }

        Arrays.stream(ids).parallel().forEach(id -> {
            studentList.add(
                    studentRepository.findById(id)
                            .map(StudentSheetMapper::mapToDTO)
                            .orElseThrow()
            );
        });

        return studentList;
    }
}
