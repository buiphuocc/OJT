package com.group1.FresherAcademyManagementSystem.services.Impl;

import com.group1.FresherAcademyManagementSystem.exceptions.ClassEntityNotFoundException;
import com.group1.FresherAcademyManagementSystem.models.ClassEntity;
import com.group1.FresherAcademyManagementSystem.models.Student;
import com.group1.FresherAcademyManagementSystem.models.Student_Class;
import com.group1.FresherAcademyManagementSystem.repositories.ClassRepository;
import com.group1.FresherAcademyManagementSystem.repositories.StudentClassRepository;
import com.group1.FresherAcademyManagementSystem.repositories.StudentRepository;
import com.group1.FresherAcademyManagementSystem.services.ExcelUploadService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
public class ExcelUploadServiceImpl implements ExcelUploadService {


    private StudentClassRepository studentClassRepository;
    private ClassRepository classRepository;
    private StudentRepository studentRepository;

    @Autowired
    public ExcelUploadServiceImpl(StudentClassRepository studentClassRepository, ClassRepository classRepository,StudentRepository studentRepository) {
        this.studentClassRepository = studentClassRepository;
        this.classRepository = classRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public boolean isValidExcelFile(MultipartFile file){
        String fileName = file.getOriginalFilename();
        if (fileName != null && (fileName.endsWith(".xlsx") || fileName.endsWith(".xltx"))) {
            return true;
        }
        return false;
    }
    public List<Student> getStudentDataFromExcel(InputStream inputStream,String classId) {
        List<Student> students = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheet("Students");
            if (sheet == null) {
                throw new UnsupportedOperationException("Sheet is not found");
            }
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) continue;
                Student student = new Student();
                boolean hasMandatoryFields = true; // Flag to check if all mandatory fields are present
                for (int cellIndex = 0; cellIndex < 16; cellIndex++) {
                    Cell cell = row.getCell(cellIndex);
                    if (cell == null) {
                        // Handle missing cell error if needed
                        continue;
                    }
                    try {
                        switch (cellIndex) {
                            case 0:
                                if (cell.getCellType() == CellType.STRING) {
                                    student.setId(cell.getStringCellValue());
                                } else if (student.getId() == null || student.getId().isEmpty()) {
                                    throw new UnsupportedOperationException("Error: ID is missing or empty");
                                } else if (cell.getCellType() == CellType.BLANK) {
                                    hasMandatoryFields = false;
                                } else {
                                    throw new UnsupportedOperationException("Error in cell " + cell.getAddress() + ": ID should be numeric");
                                }
                                break;
                            case 1:
                                Cell fullName = row.getCell(1);
                                if (fullName.getCellType() == CellType.STRING && fullName.getStringCellValue() != null) {
                                    student.setFullName(fullName.getStringCellValue());
                                    if(student.getFullName() == null) {
                                        throw new UnsupportedOperationException("EM2: The mandatory fields are missing") ;
                                    }
                                }
                                else if(fullName.getCellType() == CellType.BLANK){
                                    hasMandatoryFields = false;
                                } else {
                                    throw new UnsupportedOperationException("Error in cell " + cell.getAddress() + ": Full Name should be text");
                                }
                                break;
                            case 2:
                            {
                                Date dateObject;

                                if (cell.getCellType() == CellType.STRING) {
                                    // If the cell contains a string, try to parse it directly
                                    String dobString = cell.getStringCellValue();
                                    try {
                                        dateObject = new SimpleDateFormat("yyyy-MM-dd").parse(dobString);
                                        student.setDob(dateObject);
                                    } catch (ParseException e) {
                                        throw new UnsupportedOperationException("Invalid Date of Birth format at row " + rowIndex, e);
                                    }
                                } else if (cell.getCellType() == CellType.NUMERIC) {
                                    // If the cell contains a numeric value, convert it to a LocalDate
                                    LocalDate localDate = cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                                    // Set the Date object to the Student object
                                    dateObject = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                                    student.setDob(dateObject);
                                } else if(cell.getCellType() == CellType.BLANK){
                                    hasMandatoryFields = false;
                                }else  {
                                    // Handle other cell types if necessary
                                    throw new UnsupportedOperationException("Error in cell " + cell.getAddress() + "Date of Birth should be in format");
                                }
                            }
                            break;
                            case 3:
                                if (cell.getCellType() == CellType.STRING) {
                                    student.setEmail(cell.getStringCellValue());
                                }
                                else if(cell.getCellType() == CellType.BLANK){
                                    throw new UnsupportedOperationException("EM2: The mandatory fields are missing") ;
                                } else {
                                    throw new UnsupportedOperationException("Error in cell " + cell.getAddress() + ": Email should be text");
                                }
                                break;
                            case 4:
                                if (cell.getCellType() == CellType.NUMERIC) {
                                    student.setPhone(String.valueOf((int) cell.getNumericCellValue()));
                                } else if (cell.getCellType() == CellType.STRING) {
                                    String phone = cell.getStringCellValue();
                                    if (phone.matches("[0-9]+")) { // Kiểm tra xem chuỗi chỉ chứa các số hay không
                                        student.setPhone(phone);
                                    } else {
                                        throw new UnsupportedOperationException("Error in cell " + cell.getAddress() + ": Phone should contain only numbers");
                                    }
                                } else {
                                    throw new UnsupportedOperationException("Error in cell " + cell.getAddress() + ": Phone should be either numeric or string");
                                }
                                break;
                            case 5 :
                                if (cell.getCellType() == CellType.NUMERIC) {
                                    student.setGpa((float) cell.getNumericCellValue());
                                } else if (cell.getCellType() == CellType.STRING) {
                                    String gpaString = cell.getStringCellValue().replace(',', '.'); // replace comma with dot
                                    try {
                                        float gpa = Float.parseFloat(gpaString);
                                        student.setGpa(gpa);
                                    } catch (NumberFormatException e) {
                                        throw new UnsupportedOperationException("Invalid GPA format at row " + rowIndex, e);
                                    }
                                } else {
                                    throw new UnsupportedOperationException("Error in cell " + cell.getAddress() + ": Phone should be a number");
                                }
                                break;
                            case 6:
                                if (cell.getCellType() == CellType.STRING) {
                                    student.setRECer(cell.getStringCellValue());
                                } else {
                                    throw new UnsupportedOperationException("Error in cell " + cell.getAddress() + ": RECer should be text");
                                }
                                break;
                            case 7:
                                if (cell.getCellType() == CellType.STRING) {
                                    student.setGender(cell.getStringCellValue());
                                } else {
                                    throw new UnsupportedOperationException("Error in cell " + cell.getAddress() + ": Gender should be text");

                                }
                                break;
                            case 8:
                            {
                                    if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                                    try {
                                        Instant instant = cell.getDateCellValue().toInstant();
                                    LocalDate dateTime = instant.atZone(ZoneId.systemDefault()).toLocalDate();
                                    student.setGraduatedDate(LocalDate.from(dateTime));
                                    } catch (DateTimeParseException e) {
                                        throw new UnsupportedOperationException("Invalid Date format: " + cell.getStringCellValue());
                                    }
                                } else {
                                    throw new UnsupportedOperationException("Error in cell " + cell.getAddress() + ": Graduated Date should be in date format");
                                }
                            }
                            break;
                            case 9:
                                if (cell.getCellType() == CellType.STRING) {
                                    student.setAddress(cell.getStringCellValue());
                                } else {
                                    throw new UnsupportedOperationException("Error in cell " + cell.getAddress() + ": Address should be text");
                                }
                                break;
                            case 10:
                                if (cell.getCellType() == CellType.STRING) {
                                    student.setArea(cell.getStringCellValue());
                                } else {
                                    throw new UnsupportedOperationException("Error in cell " + cell.getAddress() + ": Area should be text");
                                }
                                break;
                            case 11:
                                if (cell.getCellType() == CellType.STRING) {
                                    student.setFAAccount((cell.getStringCellValue()));
                                } else {
                                    throw new UnsupportedOperationException("Error in cell " + cell.getAddress() + ": FA Account should be text");
                                }
                                break;
                            case 12:
                                if (cell.getCellType() == CellType.STRING) {
                                    student.setMajor(cell.getStringCellValue());
                                } else {
                                    throw new UnsupportedOperationException("Error in cell " + cell.getAddress() + ": Major should be text");
                                }
                                break;
                            case 13:
                                if (cell.getCellType() == CellType.STRING) {
                                    student.setSchool(cell.getStringCellValue());
                                } else {
                                    throw new UnsupportedOperationException("Error in cell " + cell.getAddress() + ": School should be text");
                                }
                                break;

                            case 14:
                                if (cell.getCellType() == CellType.STRING) {
                                    student.setStatus((cell.getStringCellValue()));
                                } else if(cell.getCellType() == CellType.BLANK){
                                    throw new UnsupportedOperationException("EM2: The mandatory fields are missing") ;
                                }else {
                                    throw new UnsupportedOperationException("Error in cell " + cell.getAddress() + ": Status should be text");
                                }
                                break;
                            case 15:
                                if (cell.getCellType() == CellType.STRING) {
                                    student.setType(cell.getStringCellValue());
                                } else {
                                    throw new UnsupportedOperationException("Error in cell " + cell.getAddress() + ": Type should be text");
                                }
                                break;

                            default:
                                break;
                        }
                    } catch (Exception e) {
                        throw new UnsupportedOperationException("EM3 The datatype is incorrect.");
                    }
                    if(cellIndex == 0 || cellIndex == 1 || cellIndex == 2) {
                        if (cell == null || cell.getCellType() == CellType.BLANK) {
                            hasMandatoryFields = false;
                        }
                    }
                }
                if(!hasMandatoryFields) {
                    throw new UnsupportedOperationException("EM2: The mandatory fields are missing") ;
                } else {
                    students.add(student);
                }
            }
        } catch (IOException e) {
            throw new UnsupportedOperationException("EM1: The file format is incorrect");
        }
        if (!errors.isEmpty()) {
            throw new UnsupportedOperationException("The file import process hasn't been completed");
        }
        return students;
    }

    @Override
    public ResponseEntity<byte[]> exportXLSSheet() throws IOException {
        try (FastByteArrayOutputStream fstream = new FastByteArrayOutputStream()) {
            XSSFWorkbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Students");

            Row title_row = sheet.createRow(0);

            title_row.createCell(0).setCellValue("ID");
            title_row.createCell(1).setCellValue("Full name");
            title_row.createCell(2).setCellValue("Date of birth");
            title_row.createCell(3).setCellValue("Email");
            title_row.createCell(4).setCellValue("Phone");
            title_row.createCell(5).setCellValue("GPA");
            title_row.createCell(6).setCellValue("RECer");
            title_row.createCell(7).setCellValue("Gender");
            title_row.createCell(8).setCellValue("Graduated Date");
            title_row.createCell(9).setCellValue("Address");
            title_row.createCell(10).setCellValue("Area");
            title_row.createCell(11).setCellValue("FA Account");
            title_row.createCell(12).setCellValue("Major");
            title_row.createCell(13).setCellValue("School");
            title_row.createCell(14).setCellValue("Status");
            title_row.createCell(15).setCellValue("Type");

            workbook.write(fstream);
            workbook.close();

            byte[] bytes = fstream.toByteArray();
            String base64Encoded = Base64.getEncoder().encodeToString(bytes);

            // Trả về mảng byte của chuỗi Base64, không cần chuyển đổi thành mảng byte mới
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(base64Encoded.getBytes());
        }
    }

}