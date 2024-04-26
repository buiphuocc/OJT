package com.group1.FresherAcademyManagementSystem.services.Impl;

import com.group1.FresherAcademyManagementSystem.dtos.*;
import com.group1.FresherAcademyManagementSystem.exceptions.ClassEntityNotFoundException;
import com.group1.FresherAcademyManagementSystem.exceptions.StudentNotFoundException;
import com.group1.FresherAcademyManagementSystem.mappers.ModuleMapper;
import com.group1.FresherAcademyManagementSystem.mappers.ScoreMapper;
import com.group1.FresherAcademyManagementSystem.mappers.StudentMapper;
import com.group1.FresherAcademyManagementSystem.models.*;
import com.group1.FresherAcademyManagementSystem.models.ClassEntity;
import com.group1.FresherAcademyManagementSystem.models.ClassEntity;
import com.group1.FresherAcademyManagementSystem.models.*;
import com.group1.FresherAcademyManagementSystem.models.Module;
import com.group1.FresherAcademyManagementSystem.models.Student;
import com.group1.FresherAcademyManagementSystem.models.TrainingProgram_Module;
import com.group1.FresherAcademyManagementSystem.repositories.*;
import com.group1.FresherAcademyManagementSystem.services.ScoreService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScoreServiceImpl implements ScoreService {

    private final StudentRepository studentRepository;

    private final ScoreRepository scoreRepository;


    private final StudentClassRepository studentClassRepository;

    private final ClassStudentRepository classStudentRepository;

    private final AssignmentRepository assignmentRepository;

    private final ModuleRepository moduleRepository;

    private final ClassRepository classRepository;

    private final TrainingProgramModuleRepository trainingProgramModuleRepository;


    @Override
    public Class_Score_DetailDto getScoreDetail(String studentId, String classId) {
        ClassEntity classEntity = classRepository.findById(classId).orElseThrow(() -> new ClassEntityNotFoundException("class not found !"));
        List<TrainingProgram_Module> trainingProgram_modules = trainingProgramModuleRepository.findAllByProgramId(classEntity.getTrainingProgram().getId());
        List<Module> moduleList = trainingProgram_modules.stream().map(item -> item.getModule()).collect(Collectors.toList());

        List<ModuleDto> moduleDtos = moduleList.stream().map(item -> ModuleMapper.mapToDto(item, studentId)).collect(Collectors.toList());

        Class_Score_DetailDto classScoreDetailDto = new Class_Score_DetailDto();
        classScoreDetailDto.setClassName(classEntity.getClassName());
        classScoreDetailDto.setTraningProgram(classEntity.getTrainingProgram().getName());
        classScoreDetailDto.setModules(moduleDtos);


        return classScoreDetailDto;
    }

    @Override
    public List<Class_Score_DetailDto2> viewScoreDetail(String classId) {

        ClassEntity classEntity = classRepository.findById(classId).orElseThrow(() -> new ClassEntityNotFoundException("class not found !"));

        List<Student_Class> students = studentClassRepository.findAllByClassEntity_Id(classId);
        Set<String> seen = new HashSet<>();
        List<Student_Class> uniqueStudents = new ArrayList<>();

        for (Student_Class x : students) {
            String compositeKey = x.getStudent().getId() + "-" + x.getClassEntity().getId(); // Tạo composite key từ studentId và classId
            if (seen.add(compositeKey)) {
                uniqueStudents.add(x);
            }
        }

        List<Student_Class> checkStatus = new ArrayList<>();
        for (Student_Class x : uniqueStudents){
            if(x.getAttendingStatus().equalsIgnoreCase("inclass".trim())){
                checkStatus.add(x);
            }
        }

        List<TrainingProgram_Module> trainingProgram_modules = trainingProgramModuleRepository.findAllByProgramId(classEntity.getTrainingProgram().getId());
        List<Module> moduleList = trainingProgram_modules.stream().map(item -> item.getModule()).collect(Collectors.toList());
        List<Class_Score_DetailDto2> classScoreDetailDto = new ArrayList<>();

        for (Student_Class x : checkStatus) {
            Class_Score_DetailDto2 y = new Class_Score_DetailDto2();
            List<ModuleDto> moduleDtos = moduleList.stream().map(item -> ModuleMapper.mapToDto(item, x.getStudent().getId())).collect(Collectors.toList());
            y.setId(x.getStudent().getId());
            y.setFullName(x.getStudent().getFullName());
            y.setResult(getClassResult(moduleList, x.getStudent().getId(), classId));
            y.setModules(moduleDtos);
            classScoreDetailDto.add(y);
        }

        return classScoreDetailDto;
    }
    
    public String getClassResult(List<Module> moduleList, String studentId, String classId) {
        Student_Class studentClass = studentClassRepository.findByStudentIdAndClassEntityId(studentId, classId).get();
        List<Long> moduleIdList = moduleList.stream().map(Module::getId).toList();
        List<Long> assignmentIdList = assignmentRepository
                .findByModuleIdIn(moduleIdList)
                .stream()
                .map(Assignment::getId)
                .toList();
        List<Score> scoreListOfStudent = scoreRepository.findAllByStudent_IdAndAssignmentIdIn(studentId, assignmentIdList);
        boolean hasZeroPoint = false;
        boolean hasNullPoint = false;
        float totalPoint = 0;
        int pointColumnNumber = 0;
        for (Score score : scoreListOfStudent) {
            if (score.getScore() == null) {
                hasNullPoint = true;
                break;
            } else if (score.getScore() == 0) {
                hasZeroPoint = true;
            }
            totalPoint += score.getScore();
            pointColumnNumber++;
        }
        float finalScore = totalPoint / (float) pointColumnNumber;
        finalScore = Float.parseFloat(String.format("%.2f", finalScore));
        if (hasNullPoint) {
            studentClass.setResult("inprogress");
            studentClassRepository.save(studentClass);
            return "inprogress";
        } else if (hasZeroPoint || finalScore < 5.0) {
            studentClass.setResult("not passed");
            studentClass.setFinalScore(finalScore);
            studentClassRepository.save(studentClass);
            return "not passed";
        } else if (finalScore <= 10.0) {
            studentClass.setResult("passed");
            studentClass.setFinalScore(finalScore);
            studentClassRepository.save(studentClass);
            return "passed";
        }
        return "Invalid result";
    }

    @Override
    public void updateScocesStudentDetailInClass(List<ScoreDto2> scores) {
        for (ScoreDto2 x : scores) {
            Score update = scoreRepository.findById(x.getId()).orElseThrow();
            update.setScore(x.getScore());
            scoreRepository.save(update);
        }
    }
    private void updateScoresByFileExcel(InputStream inputStream) {
        List<String> errors = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheet("Scores");
            if (sheet == null) {
                throw new UnsupportedOperationException("Sheet 'Scores' is not found");
            }
            String temp1 = null;
            Long temp2 = null;
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) continue;
                boolean hasMandatoryFields = true;

                for (int cellIndex = 0; cellIndex < 4; cellIndex++) {
                    Cell cell = row.getCell(cellIndex);
                    try {
                    switch (cellIndex) {
                        case 0:
                            if (cell.getCellType() == CellType.STRING) {
                                String studentId = cell.getStringCellValue();
                                if (studentId == null || studentId.trim().isEmpty()) {
                                    errors.add("Error in cell " + cell.getAddress() + ": Student ID is missing");
                                } else {
                                    Student student = studentRepository.findById(studentId).orElseThrow(() -> new StudentNotFoundException("Student Not Found"));
                                    temp1 = studentId;
                                }
                            } else{
                                errors.add("Error in cell " + cell.getAddress() + ": Student ID is missing");
                            }
                            break;
                        case 1:
                            if (cell.getCellType() == CellType.STRING) {
                                String fullName = cell.getStringCellValue();
                                if (fullName == null || fullName.trim().isEmpty()) {
                                    errors.add("Error in cell " + cell.getAddress() + ": Full Name is missing");
                                } else {
                                    Student student = studentRepository.findById(temp1).orElseThrow(() -> new StudentNotFoundException("Student Not Found"));
                                    student.setFullName(fullName);
                                }
                            } else {
                                errors.add("Error in cell " + cell.getAddress() + ": FullName should be a string");
                            }
                            break;
                        case 2:
                            if (cell.getCellType() == CellType.NUMERIC) {
                                Long scoreId = (long) cell.getNumericCellValue();
                                Score scores = scoreRepository.findById(scoreId).orElseThrow(() -> new EntityNotFoundException("Not found Score!"));
                                if(scores != null)
                                    temp2 = scoreId;
                                if (scores.getId() == null) {
                                    throw new UnsupportedOperationException("EM2: The mandatory fields are missing");
                                }
                            } else if (cell.getCellType() == CellType.BLANK) {
                                throw new UnsupportedOperationException("EM2: The mandatory fields are missing");
                            } else {
                                throw new UnsupportedOperationException("Error in cell " + cell.getAddress() + ": Score should be numeric");
                            }
                            break;
                        case 3:
                            if(cell != null && cell.getCellType() == CellType.NUMERIC) {
                                Float scoreNum = (float) cell.getNumericCellValue();
                                Score scores = scoreRepository.findById(temp2).orElseThrow(() -> new EntityNotFoundException("Not found !"));
                                if(scores != null) {
                                    scores.setScore(scoreNum);
                                    scoreRepository.save(scores);
                                }
                            } else if(cell ==null) {
                                Score scores = scoreRepository.findById(temp2).orElseThrow(() -> new EntityNotFoundException("Not found !"));
                                if(scores != null) {
                                    scores.setScore(null);
                                    scoreRepository.save(scores);
                                }
                            } else if(cell.getCellType() == CellType.STRING) {
                                throw new UnsupportedOperationException("Error in cell " + cell.getAddress() + ": Score should be numeric");
                            }
                            break;
                        default:
                            break;
                    }
                    }catch(Exception e) {
                        throw new UnsupportedOperationException("EM3 The datatype is incorrect.");
                    }
                }
            }
        } catch (IOException e) {
            throw new UnsupportedOperationException("EM1: The file format is incorrect");
        }
        if (!errors.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder("Error(s) occurred while processing the file:\n");
            for (String error : errors) {
                errorMessage.append(error).append("\n");
            }
            throw new UnsupportedOperationException(errorMessage.toString());
        }
    }




    @Override
    public boolean isValidExcelFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName != null && (fileName.endsWith(".xlsx") || fileName.endsWith(".xltx"))) {
            return true;
        }
        return false;
    }
    @Override
    public List<String> getStudentIdsOfClass(String classId) {
        List<Student_Class> studentsInClass = studentClassRepository.findByClassEntityId(classId);
        return studentsInClass.stream()
                .map(studentClass -> studentClass.getStudent().getId())
                .collect(Collectors.toList());
    }
    @Override
    public void writeScoresToExcel(List<Score> scores, String filePath, String classId) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Scores");

            Row headersRow = sheet.createRow(0);
            headersRow.createCell(0).setCellValue("StudentID");
            headersRow.createCell(1).setCellValue("Fullname");
            headersRow.createCell(2).setCellValue("ID");
            headersRow.createCell(3).setCellValue("Score");
            ClassEntity classEntity = classRepository.findById(classId).orElseThrow(() -> new ClassEntityNotFoundException("class not found !"));
            List<TrainingProgram_Module> trainingProgram_modules = trainingProgramModuleRepository.findAllByProgramId(classEntity.getTrainingProgram().getId());
            List<Module> moduleList = trainingProgram_modules.stream().map(item -> item.getModule()).collect(Collectors.toList());
            List<ScoreListDto> moduleDtos = new ArrayList<>();
            moduleList.stream().forEach(item -> {
                for(Assignment x: item.getAssignments()) {
                    for (Score x2: x.getScores()) {
                        if(studentClassRepository.existsByStudentIdAndClassEntityId(x2.getStudent().getId(), classId)){
                            ScoreListDto scoreListDto = new ScoreListDto();
                            scoreListDto.setStudentId(x2.getStudent().getId());
                            scoreListDto.setFullName(x2.getStudent().getFullName());
                            scoreListDto.setId(x2.getId());
                            scoreListDto.setScore(x2.getScore());
                            moduleDtos.add(scoreListDto);
                        }
                    }
                }
            });
            for (ScoreListDto scoreListDto: moduleDtos) {
                // Tạo một hàng mới cho điểm và sao chép thông tin sinh viên vào cùng hàng
                Row scoreRow = sheet.createRow(sheet.getLastRowNum() + 1);
                scoreRow.createCell(0).setCellValue(scoreListDto.getStudentId()); // StudentID
                scoreRow.createCell(1).setCellValue(scoreListDto.getFullName()); // Fullname
                scoreRow.createCell(2).setCellValue(scoreListDto.getId()); // ID
                if (scoreListDto.getScore() != null) {
                    scoreRow.createCell(3).setCellValue(scoreListDto.getScore()); // Score
                } else {
                    scoreRow.createCell(3).setCellValue(""); // Set cell value to empty if score is null
                }
            }
            // Ghi file
            Path path = Paths.get(filePath);
            try (FileOutputStream fileOut = new FileOutputStream(path.toFile())) {
                workbook.write(fileOut);
                System.out.println("Excel file has been generated successfully at: " + path.toAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public List<Score> getAllScores() {
        return scoreRepository.findAll();
    }

    @Override
    public String saveScoresToDatabase(MultipartFile file) {
        try {
            if (isValidExcelFile(file)) {
                updateScoresByFileExcel(file.getInputStream());
            } else {
                // Handle invalid Excel file
                throw new UnsupportedOperationException("Invalid Excel File");
            }
        } catch (IOException e) {
            throw new UnsupportedOperationException("The file is not a valid Excel file");
        }
        return null;
    }

}
