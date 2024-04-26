package com.group1.FresherAcademyManagementSystem.services.Impl;


import com.group1.FresherAcademyManagementSystem.dtos.*;
import com.group1.FresherAcademyManagementSystem.exceptions.*;
import com.group1.FresherAcademyManagementSystem.mappers.ClassEntityMapper;
import com.group1.FresherAcademyManagementSystem.mappers.ModuleMapper;
import com.group1.FresherAcademyManagementSystem.mappers.StudentMapper;
import com.group1.FresherAcademyManagementSystem.models.Module;
import com.group1.FresherAcademyManagementSystem.models.*;
import com.group1.FresherAcademyManagementSystem.repositories.*;
import com.group1.FresherAcademyManagementSystem.services.ExcelUploadService;
import com.group1.FresherAcademyManagementSystem.services.StudentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@CacheConfig(cacheNames = "student")
@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    private final ExcelUploadService excelUploadService;

    private final ModelMapper modelMapper;

    private final StudentClassRepository studentClassRepository;

    private final ClassStudentRepository classStudentRepository;

    private final ClassRepository classRepository;

    private final TrainingProgramModuleRepository trainingProgramModuleRepository;

    private final ReserveClassRepository reserveClassRepository;

    private final AssignmentRepository assignmentRepository;

    private final ScoreRepository scoreRepository;

    private static SimpleDateFormat getSimpleDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd");
    }

    @Override
    public void deleteStudentById(String studentId) {
        Student existingStudent = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with ID: " + studentId));
        // Kiểm tra xem sinh viên có trạng thái là 'inactive' không
        if (!"inactive".trim().equalsIgnoreCase(existingStudent.getStatus())) {
            throw new UnsupportedOperationException("Only 'Inactive' students can be delete.");
        }
        existingStudent.setStatus("disable".trim());
        // Lưu sinh viên đã cập nhật vào cơ sở dữ liệu
        studentRepository.save(existingStudent);
    }

    @Override
    public void deleteStudentByIdInClass(String studentId, String classId) {
        List<Student_Class> studentInClassList = studentClassRepository.findByStudentIdAndClassId(studentId, classId);
        List<Student_Class> studentsToDelete = new ArrayList<>();
        if (studentInClassList.isEmpty()) {
            throw new EntityNotFoundException("Student not Found" + studentId);
        }
        for (Student_Class studentClass : studentInClassList) {
            if (studentClass.getClassEntity().getStatus().equalsIgnoreCase("inprogress".trim()) ||
                    studentClass.getClassEntity().getStatus().equalsIgnoreCase("planning".trim())) {

                List<TrainingProgram> trainingPrograms = new ArrayList<>();
                trainingPrograms.add(studentClass.getClassEntity().getTrainingProgram());

                boolean hasScore = hasScoreInTrainingProgram(studentId, trainingPrograms);
                if (!hasScore) {
                    studentClass.setAttendingStatus("deleted".trim());
                    studentsToDelete.add(studentClass);
                } else {
                    throw new UnsupportedOperationException("Student has score cannot be deleted");
                }
            } else if (studentClass.getClassEntity().getStatus().equalsIgnoreCase("finished".trim())) {
                throw new UnsupportedOperationException("This class has already finished");
            }
        }
        studentClassRepository.saveAll(studentsToDelete);
    }


    private boolean hasScoreInTrainingProgram(String studentId, List<TrainingProgram> trainingPrograms) {
        for (TrainingProgram trainingProgram : trainingPrograms) {
            List<TrainingProgram_Module> modules = trainingProgram.getTrainingProgram_modules();
            for (TrainingProgram_Module module : modules) {
                List<Assignment> assignments = module.getModule().getAssignments();
                for (Assignment assignment : assignments) {
                    List<Score> scores = assignment.getScores();
                    for (Score score : scores) {
                        if (score.getStudent().getId().equalsIgnoreCase(studentId)) {
                            return true; // Nếu sinh viên có điểm, trả về true
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public StudentDto getStudentById(String studentId) {
        Student student = studentRepository.findById(studentId)
                .orElse(null);
        return StudentMapper.mapToDto(student);
    }

    @Override
    public StudentDto editStudentbyReserve(String studentId, StudentDto updatedStudentDetails) {
        Student existingStudent = studentRepository.findById(studentId)
                .orElse(null);
        existingStudent.setId(updatedStudentDetails.getId());
        existingStudent.setFAAccount(updatedStudentDetails.getFAAccount());
        existingStudent.setRECer(updatedStudentDetails.getRECer());
        existingStudent.setAddress(updatedStudentDetails.getAddress());
        existingStudent.setArea(updatedStudentDetails.getArea());
        existingStudent.setDob(updatedStudentDetails.getDob());
        existingStudent.setEmail(updatedStudentDetails.getEmail());
        existingStudent.setFullName(updatedStudentDetails.getFullName());
        existingStudent.setGender(updatedStudentDetails.getGender());
        existingStudent.setGpa(updatedStudentDetails.getGpa());
        existingStudent.setGraduatedDate(updatedStudentDetails.getGraduatedDate());
        existingStudent.setJoinDate(updatedStudentDetails.getJoinDate());
        existingStudent.setMajor(updatedStudentDetails.getMajor());
        existingStudent.setPhone(updatedStudentDetails.getPhone());
        existingStudent.setSchool(updatedStudentDetails.getSchool());
        existingStudent.setStatus(updatedStudentDetails.getStatus());
        existingStudent.setType(updatedStudentDetails.getType());
        return StudentMapper.mapToDto(existingStudent);
    }

    @Override
    public StudentCreateDto createStudent(StudentCreateDto studentCreateDto) {
        String getBiggestId = studentRepository.findTopByOrderByIdDesc().map(Student::getId).orElse("ST00");
        String[] partedId = {getBiggestId.substring(0, 2), getBiggestId.substring(2)};

        Integer incrementValueInId = Integer.parseInt(partedId[1]);
        incrementValueInId++;
        incrementValueInId.toString();
        String formatNumber = String.format("%02d", incrementValueInId);
        String newStudentId = "ST" + formatNumber;

        Optional<Student> checkExist = studentRepository.findById(newStudentId);
        if (checkExist.isEmpty()) {
            Student student = new Student(
                    newStudentId,
                    studentCreateDto.getFullName(),
                    studentCreateDto.getDob(),
                    studentCreateDto.getGender(),
                    studentCreateDto.getPhone(),
                    studentCreateDto.getEmail(),
                    studentCreateDto.getSchool(),
                    studentCreateDto.getMajor(),
                    studentCreateDto.getGraduatedDate(),
                    studentCreateDto.getGpa(),
                    studentCreateDto.getAddress(),
                    null, null,
                    "active",
                    studentCreateDto.getRECer(),
                    null,
                    studentCreateDto.getArea(),
                    null, null, null, null, null
            );

            if (studentCreateDto.getClassId() != null) {
                ClassEntity addClass = classRepository.findById(studentCreateDto.getClassId()).orElseThrow(() -> new NoSuchElementException("Class not found"));
                if (!addClass.getStatus().equals("finished".trim())) {
                    studentRepository.save(student);
                    Student_Class studentClass = new Student_Class();
                    studentClass.setStudent(studentRepository.findById(newStudentId).orElseThrow(() -> new StudentNotFoundException("Student not found")));
                    studentClass.setClassEntity(addClass);
                    studentClass.setAttendingStatus("inclass");
                    studentClass.setResult("inprogress");
                    studentClassRepository.save(studentClass);
                } else throw new StudentExistedException("Class is finished");
            } else {
                studentRepository.save(student);
            }
        } else throw new StudentExistedException("Student already exist");

        return studentCreateDto;
    }

    @Override
    public String saveStudentsToDatabase(MultipartFile file, ImportOption importOption, String classId) {
        try {
            if (excelUploadService.isValidExcelFile(file)) {
                List<Student> students = excelUploadService.getStudentDataFromExcel(file.getInputStream(), classId);
                if (students.size() != 0) {
                    switch (importOption) {
                        case ALLOW:
                            allowImportData(students, classId);
                            break;
                        case REPLACE:
                            importDuplicates(students, classId);
                            break;
                        case SKIP:
                            skipDuplicates(students, classId);
                            break;
                        default:
                            throw new UnsupportedOperationException("Invalid import option");
                    }
                } else {
                    throw new UnsupportedOperationException("Error");
                }
            } else {
                // Handle invalid Excel file
                throw new UnsupportedOperationException("Invalid Excel File");
            }
        } catch (IOException e) {
            throw new UnsupportedOperationException("The file is not a valid Excel file");
        }
        return null;
    }

    private void allowImportData(List<Student> students, String classId) {
        List<Student_Class> student_classes = new ArrayList<>();
        boolean flag = studentClassRepository.isEmpty();
        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new ClassEntityNotFoundException("Class not found"));
        for (Student x : students) {
            if (flag && x.getStatus().equalsIgnoreCase("active")) {
                Student_Class student_class = new Student_Class();
                student_class.setStudent(x);
                student_class.setClassEntity(classRepository.findById(classId).orElseThrow(() -> new ClassEntityNotFoundException("not found")));
                student_class.setGpaLevel((float) 1);
                student_class.setAttendingStatus("inclass");
                student_classes.add(student_class);

            } else if (!flag && x.getStatus().equalsIgnoreCase("active")) {
                Student_Class student_class = new Student_Class();
                student_class.setStudent(x);
                student_class.setGpaLevel((float) 1);
                student_class.setClassEntity(classRepository.findById(classId).orElseThrow(() -> new ClassEntityNotFoundException("not found")));
                student_class.setAttendingStatus("inclass");
                student_classes.add(student_class);
            }
        }
        studentClassRepository.saveAll(student_classes);
    }

    private void updateStudentInformation(Student existingStudent, Student importedStudent) {
        existingStudent.setFullName(importedStudent.getFullName());
        existingStudent.setDob(importedStudent.getDob());
        existingStudent.setGender(importedStudent.getGender());
        existingStudent.setPhone(importedStudent.getPhone());
        existingStudent.setEmail(importedStudent.getEmail());
        existingStudent.setSchool(importedStudent.getSchool());
        existingStudent.setMajor(importedStudent.getMajor());
        existingStudent.setGraduatedDate(importedStudent.getGraduatedDate());
        existingStudent.setGpa(importedStudent.getGpa());
        existingStudent.setAddress(importedStudent.getAddress());
        existingStudent.setFAAccount(importedStudent.getFAAccount());
        existingStudent.setType(importedStudent.getType());
        existingStudent.setStatus(importedStudent.getStatus());
        existingStudent.setArea(importedStudent.getArea());
        existingStudent.setRECer(importedStudent.getRECer());
        studentRepository.save(existingStudent);
    }

    //REPLACE
    private void importDuplicates(List<Student> importedStudents, String classId) {
        List<Student_Class> student_classes = new ArrayList<>();
        for (Student x : importedStudents) {
            if (studentClassRepository.existsByStudentIdAndClassEntityId(x.getId(), classId)) {
                Student oldStudent = studentRepository.findById(x.getId()).orElseThrow(() -> new StudentNotFoundException("not found"));
                updateStudentInformation(oldStudent, x);
            } else {
                String temp = x.getStatus();
                if (temp == null) {
                    temp = "";
                }
                if (temp.equalsIgnoreCase("active")) {
                    Student_Class student_class = new Student_Class();
                    student_class.setStudent(x);
                    student_class.setGpaLevel((float) 1);
                    student_class.setClassEntity(classRepository.findById(classId).orElseThrow(() -> new ClassEntityNotFoundException("not found")));
                    student_class.setAttendingStatus("inclass");
                    student_classes.add(student_class);
                } else if (x.getStatus() == null) {
                    throw new UnsupportedOperationException("EM2: The mandatory fields are missing");
                }
            }
            studentClassRepository.saveAll(student_classes);
        }
    }

    private void skipDuplicates(List<Student> importedStudents, String classId) {
        List<Student_Class> student_classes = new ArrayList<>();
        for (Student x : importedStudents) {
            if (studentRepository.findById(x.getId()) != null) {
                if (!studentClassRepository.existsByStudentIdAndClassEntityId(x.getId(), classId)) {
                    String temp = x.getStatus();
                    if (temp == null) {
                        temp = "";
                    }
                    if (temp.equalsIgnoreCase("active")) {
                        Student_Class student_class = new Student_Class();
                        student_class.setStudent(x);
                        student_class.setClassEntity(classRepository.findById(classId).orElseThrow(() -> new ClassEntityNotFoundException("not found")));
                        student_class.setAttendingStatus("inclass");
                        student_class.setGpaLevel((float) 1);

                        student_classes.add(student_class);
                    } else if (x.getStatus() == null) {
                        throw new UnsupportedOperationException("EM2: The mandatory fields are missing");
                    }
                } else {
                    System.out.println("Skip duplicate " + x.getId());
                }
            } else throw new StudentNotFoundException("StudentNotFound");

        }
        studentClassRepository.saveAll(student_classes);
    }

    @Override
    public List<StudentByClassDto> findByStudentByClass(String classId) {
        List<Student> students = studentRepository.findByStudentClasses_ClassEntityClassId(classId);
        return mapFromStudentToStudentByClassDto(students, classId);
    }

    public List<StudentByClassDto> mapFromStudentToStudentByClassDto(List<Student> students, String classId) {
        return students.stream()
                .sorted(Comparator.comparing(Student::getId))
                .map(student -> {
                    StudentByClassDto dto = modelMapper.map(student, StudentByClassDto.class);

                    dto.setGpa(student.getStudentClasses().stream()
                            .filter(sc -> sc.getClassEntity().getId().equals(String.valueOf(classId)))
                            .findFirst()
                            .orElseThrow(() -> new NotFoundException("Class " + classId + " Not Found"))
                            .getGpaLevel());

                    dto.setDob(getSimpleDateFormat().format(student.getDob()));

                    dto.setAttendingStatus(student.getStudentClasses().stream()
                            .filter(sc -> sc.getClassEntity().getId().equals(String.valueOf(classId)))
                            .findFirst()
                            .orElseThrow(() -> new NotFoundException("Class " + classId + " Not Found"))
                            .getAttendingStatus());

                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<StudentByClassDto> findStudentByFIlter(String keyword, boolean fullName, boolean email, boolean phone, boolean dropout, boolean checkFinishStatus, String classId) {
        List<Student> students = studentRepository.findByIdOrFullNameOrEmailOrPhoneOrStatusAndClassId(keyword, fullName, email, phone, dropout, checkFinishStatus, classId);
        return mapFromStudentToStudentByClassDto(students, classId);
    }

    public List<StudentByClassDto> findStudentByAttendingStatus(boolean dropout, boolean checkFinishStatus, String classId) {
        List<Student> students = studentRepository.findByStatusAndClassId(dropout, checkFinishStatus, classId);
        return mapFromStudentToStudentByClassDto(students, classId);
    }

    @Override
    @Transactional(rollbackFor = {NoSuchElementException.class, StatusMismatchException.class})
    public Map<String, Object> updateSelectedStudentStatus(String[] studentIds, String classId, String status) {
        Map<String, Object> response_object = new HashMap<>();
        List<Student_Class> update_list = new ArrayList<>();

        var first_student = studentClassRepository.findByStudentIdAndClassEntityId(studentIds[0], classId)
                .orElseThrow(() -> new NoSuchElementException("Student \"" + studentIds[0] + "\" cannot be found"));

        var attending_status = first_student.getAttendingStatus().trim();

        for (String id : studentIds) {
            var current_student = studentClassRepository
                    .findByStudentIdAndClassEntityId(id, classId).orElseThrow(()
                            -> new NoSuchElementException("Student \"" + id + "\" cannot be found"));

            if (!studentClassRepository
                    .existsByStudent_IdAndAndAttendingStatus(current_student.getStudent().getId(), "inclass")) {
                var student = current_student.getStudent();
                student.setStatus("inactive");
                studentRepository.save(student);
            }

            if (!attending_status.equalsIgnoreCase(current_student.getAttendingStatus().trim())) {
                throw new StatusMismatchException();
            }

            current_student.setAttendingStatus(status);

            update_list.add(current_student);
        }


        var updated_entity = studentClassRepository.saveAll(update_list);

        var success = updated_entity.stream().parallel()
                .filter(studentClass -> studentClass.getAttendingStatus().equalsIgnoreCase(status))
                .toList();

        if (!success.isEmpty())
            response_object.put("success", success);

        return response_object;
    }

    @Override
    public List<StudentListSystemDto> getAllStudent() {
        List<Student> studentList = studentRepository.findAll();

        List<StudentListSystemDto> studentListSystemDtos = new ArrayList<>();
        for (Student student : studentList) {
            List<Student_Class> studentClassList = studentClassRepository.findAllByStudent_Id(student.getId());
            List<ClassListDto> classListDtos = new ArrayList<>();
            for (Student_Class studentClass : studentClassList) {
                ClassListDto classOfStudent = ClassEntityMapper.mapToClassListDto(classRepository.findById(studentClass.getClassEntity().getId()).orElseThrow(() -> new StudentNotFoundException("Class Not Found")));
                classListDtos.add(classOfStudent);
            }
            StudentListSystemDto studentListSystemDto = new StudentListSystemDto(
                    student.getId(),
                    student.getFullName(),
                    student.getPhone(),
                    student.getEmail(),
                    student.getGender(),
                    student.getStatus(),
                    student.getDob(),
                    student.getRECer(),
                    student.getGpa(),
                    student.getMajor(),
                    classListDtos
            );
            studentListSystemDtos.add(studentListSystemDto);
        }
        return studentListSystemDtos;
    }

    @Override
    public List<StudentListSystemDto> getAllStudentWithReservation() {
        List<Student> studentList = studentRepository.findAll();

        List<StudentListSystemDto> studentListSystemDtos = new ArrayList<>();
        for (Student student : studentList) {
            List<Student_Class> studentClassList = studentClassRepository.findAllByStudentIdWithReservation(student.getId());
            List<ClassListDto> classListDtos = new ArrayList<>();
            for (Student_Class studentClass : studentClassList) {
                ClassListDto classOfStudent = ClassEntityMapper.mapToClassListDto(classRepository.findById(studentClass.getClassEntity().getId()).orElseThrow(() -> new StudentNotFoundException("Class Not Found")));
                classListDtos.add(classOfStudent);
                StudentListSystemDto studentListSystemDto = new StudentListSystemDto(
                        student.getId(),
                        student.getFullName(),
                        student.getPhone(),
                        student.getEmail(),
                        student.getGender(),
                        student.getStatus(),
                        student.getDob(),
                        student.getRECer(),
                        student.getGpa(),
                        student.getMajor(),
                        classListDtos
                );
                if (!studentListSystemDtos.contains(studentListSystemDto)) {
                    studentListSystemDtos.add(studentListSystemDto);
                }
            }
        }
        return studentListSystemDtos;
    }


    @Override
    public void updateInfoStudentInClass(String classId, String studentId, Student_Detail_ClassDto2 studentDetailClassDto2) {
        List<Student_Class> students = classStudentRepository.findAll();
        Set<String> seen = new HashSet<>();
        List<Student_Class> uniqueStudents = new ArrayList<>();

        for (Student_Class x : students) {
            String compositeKey = x.getStudent().getId() + "-" + x.getClassEntity().getId(); // Tạo composite key từ studentId và classId
            if (seen.add(compositeKey)) {
                uniqueStudents.add(x);
            }
        }
        for (Student_Class x : uniqueStudents) {
            if (x.getStudent().getId().equalsIgnoreCase(studentId) && x.getClassEntity().getId().equalsIgnoreCase(classId)) {
                String status = !x.getAttendingStatus().isEmpty() ? x.getAttendingStatus().trim().replace(" ", "") : "";
                if (!"InClass".equalsIgnoreCase(status)) {
                    throw new IllegalArgumentException("Status is not available to update info !!!");
                }
                GeneralDto generalDto = studentDetailClassDto2.getGeneral();
                OthersDto othersDto = studentDetailClassDto2.getOthers();
                Student student = studentRepository.findById(studentId).orElseThrow(() -> new StudentNotFoundException("not found "));
                student.setDob(generalDto.getDob());
                student.setGender(generalDto.getGender());
                student.setPhone(generalDto.getPhone());
                student.setEmail(generalDto.getEmail());
                student.setAddress(generalDto.getAddress());
                x.setAttendingStatus(generalDto.getAttendingStatus());
                x.setCertificationStatus(generalDto.getCertificationStatus());

                if (generalDto.getCertificationDate() != null) {
                    String dateString = generalDto.getCertificationDate();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate date = LocalDate.parse(dateString, formatter);
                    x.setCertificationDate(date);
                } else {
                    x.setCertificationDate(null);
                }

                student.setArea(generalDto.getArea());
                student.setSchool(othersDto.getSchool());
                student.setMajor(othersDto.getMajor());

                if (othersDto.getYearOfGraduation() != null) {
                    String dateString2 = othersDto.getYearOfGraduation(); // Example date string
                    DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate date2 = LocalDate.parse(dateString2, formatter2);
                    student.setGraduatedDate(date2);
                } else {
                    student.setGraduatedDate(null);
                }

                student.setGpa(othersDto.getGpa());
                student.setRECer(othersDto.getRecer());
                studentClassRepository.save(x);
                studentRepository.save(student);
            }
        }
    }


    @Override
    public Student_Detail_ClassDto getStudent_detail(String classId, String studentId) {
        List<Student_Class> students = classStudentRepository.findAll();
        Set<String> seen = new HashSet<>();
        List<Student_Class> uniqueStudents = new ArrayList<>();

        for (Student_Class x : students) {
            String compositeKey = x.getStudent().getId() + "-" + x.getClassEntity().getId(); // Tạo composite key từ studentId và classId
            if (seen.add(compositeKey)) {
                uniqueStudents.add(x);
            }
        }

        Student_Detail_ClassDto s1 = new Student_Detail_ClassDto();
        GeneralDto generalDto = new GeneralDto();
        OthersDto orOthersDto = new OthersDto();
        StudentDto2 studentDto = StudentMapper.maptoDto2(studentRepository.findById(studentId).orElseThrow(() -> new StudentNotFoundException("not found ")));
        Student_Class studentClass = null;
        for (Student_Class x : uniqueStudents) {
            if (x.getStudent().getId().equalsIgnoreCase(studentId) && x.getClassEntity().getId().equalsIgnoreCase(classId)) {
                studentClass = x;
            }
        }

        generalDto.setId(studentDto.getId());
        generalDto.setFullName(studentDto.getFullName());
        generalDto.setDob(studentDto.getDob());
        generalDto.setGender(studentDto.getGender());
        generalDto.setPhone(studentDto.getPhone());
        generalDto.setEmail(studentDto.getEmail());
        generalDto.setAttendingStatus(studentClass.getAttendingStatus());
        generalDto.setAddress(studentDto.getAddress());
        generalDto.setCertificationStatus(studentClass.getCertificationStatus());
        LocalDate certificationDate = studentClass.getCertificationDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (certificationDate != null) {
            String formattedDate = certificationDate.format(formatter);
            generalDto.setCertificationDate(formattedDate);
        } else {
            generalDto.setCertificationDate(null);
        }

        generalDto.setArea(studentDto.getArea());

        orOthersDto.setSchool(studentDto.getSchool());
        orOthersDto.setMajor(studentDto.getMajor());

        LocalDate yearOfGraduation = studentDto.getGraduatedDate();
        if (yearOfGraduation != null) {
            String formatYearOfGraduation = yearOfGraduation.format(formatter);
            orOthersDto.setYearOfGraduation(formatYearOfGraduation);
        } else {
            orOthersDto.setYearOfGraduation(null);
        }
        orOthersDto.setGpa(studentDto.getGpa());
        orOthersDto.setRecer(studentDto.getRECer());


        Class_Score_DetailDto s2 = new Class_Score_DetailDto();
        ClassEntity classEntity = classRepository.findById(classId).orElseThrow(() -> new ClassEntityNotFoundException("class not found !"));
        s2.setClassName(classEntity.getClassName());
        s2.setTraningProgram(classEntity.getTrainingProgram().getName());
        List<TrainingProgram_Module> trainingProgram_modules = trainingProgramModuleRepository.findAllByProgramId(classEntity.getTrainingProgram().getId());
        List<Module> moduleList = trainingProgram_modules.stream().map(TrainingProgram_Module::getModule).toList();
        List<ModuleDto> moduleDtos = moduleList.stream().map(item -> ModuleMapper.mapToDto(item, studentId)).collect(Collectors.toList());
        s2.setModules(moduleDtos);
        generalDto.setResult(getResult(moduleList, studentId, studentClass));

        s1.setGeneral(generalDto);
        s1.setOthers(orOthersDto);
        s1.setClassScoreDetail(s2);
        return s1;
    }

    public String getResult(List<Module> moduleList, String studentId, Student_Class studentClass) {
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
    public void updateStatusStudentClass(String classId, String studentId, String status) {
        Student_Class studentClass = classStudentRepository.findByStudentIdAndClassId(studentId, classId);
        studentClass.setAttendingStatus(status);
        Reserved_Class reservedClass = reserveClassRepository.findByStudentIdAndClassId(studentId, classId);
        classStudentRepository.save(studentClass);
        reserveClassRepository.delete(reservedClass);
    }

    @Override
    public void updateCertificateStatusStudentClass(String studentId, String classId, String status, LocalDate certificateDate) {
        List<Student_Class> studentClass = studentClassRepository.findByStudentIdAndClassId(studentId, classId);
        for (Student_Class student_class : studentClass) {
            student_class.setCertificationStatus(status);
            student_class.setCertificationDate(certificateDate);
        }
        classStudentRepository.saveAll(studentClass);
    }

    public String toggleDropOutClass(String classId,
                                     String studentId) {
        var classData = studentClassRepository.findByStudentIdAndClassEntityId(studentId, classId)
                .orElseThrow();

        var status = classData.getAttendingStatus();
        String updateStatus = "";

        switch (status) {
            case "In class":
            case "On going":
            case "Back to class":
                updateStatus = "Drop out";
                break;
            case "Drop out":
                updateStatus = "Back to class";
                break;
            default:
                throw new IllegalArgumentException("Class attending status is not in switchable state");
        }

        classData.setAttendingStatus(updateStatus);

        status = studentClassRepository.save(classData).getAttendingStatus();

        return status;
    }

    @Override
    public List<Student> getAllStudentsByFilter(String keyword, String[] filter, String[] viewBy) {
        boolean fullName = false;
        boolean email = false;
        boolean phone = false;
        boolean inactive = false;
        boolean disable = false;

        if (filter != null) {
            for (String filterString : filter) {
                if (filterString.equals("fullname")) fullName = true;
                if (filterString.equals("email")) email = true;
                if (filterString.equals("phone")) phone = true;
            }
        }
        if (viewBy != null) {
            for (String viewByString : viewBy) {
                if (viewByString.equals("Inactive")) inactive = true;
                if (viewByString.equals("Disable")) disable = true;
            }
        }
        if (keyword != null)
            return studentRepository.findByIdOrFullNameOrEmailOrPhoneOrStatusForExport(keyword, fullName, email, phone, inactive, disable);
        else if (inactive || disable) {
            return studentRepository.findByStatusForExport(inactive, disable);
        }
        return studentRepository.findAllForExport();
    }

    @Override
    public Page<Student> getStudentSearchByFilter(int pageNumber, int pageSize, String sortField, String sortDir, String keyword, String[] filter, String[] viewBy) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        boolean fullName = false;
        boolean email = false;
        boolean phone = false;
        boolean inactive = false;
        boolean disable = false;

        if (filter != null) {
            for (String filterString : filter) {
                if (filterString.equals("fullname")) fullName = true;
                if (filterString.equals("email")) email = true;
                if (filterString.equals("phone")) phone = true;
            }
        }
        if (viewBy != null) {
            for (String viewByString : viewBy) {
                if (viewByString.equals("Inactive")) inactive = true;
                if (viewByString.equals("Disable")) disable = true;
            }
        }
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        if (keyword != null)
            return studentRepository.findByIdOrFullNameOrEmailOrPhoneOrStatus(keyword, pageable, fullName, email, phone, inactive, disable);
        else if (inactive || disable) {
            return studentRepository.findByStatus(pageable, inactive, disable);
        }
        return studentRepository.findAll(pageable);
    }

    @Override
    public List<StudentByClassDto> getAllStudentsInClassByFilter(String value, String[] listFilter, String[] viewByStatus, String classId) {
        boolean fullName = false;
        boolean email = false;
        boolean phone = false;
        boolean dropout = false;
        boolean checkFinishedStatus = false;
        ClassEntity currenrClass = classRepository.findById(classId).orElseThrow(() -> new ClassEntityNotFoundException("not found "));
        checkFinishedStatus = currenrClass.getStatus().equalsIgnoreCase("finished");

        if (listFilter != null) {
            for (String filterString : listFilter) {
                if (filterString.equals("fullname")) fullName = true;
                if (filterString.equals("email")) email = true;
                if (filterString.equals("phone")) phone = true;
            }
        }
        if (viewByStatus != null) {
            for (String viewByString : viewByStatus) {
                if (viewByString.equals("dropout")) {
                    dropout = true;
                    break;
                }
            }
        }
        List<StudentByClassDto> resultList;
        if (value != null)
            resultList = findStudentByFIlter(value, fullName, email, phone, dropout, checkFinishedStatus, classId);
        else {
            resultList = findStudentByAttendingStatus(dropout, checkFinishedStatus, classId);
        }
        return resultList;
    }

    @Override
    public StudentReserveDetailDto getStudentReserveDetail(Long reservedId) {
        Reserved_Class reservedClass = reserveClassRepository.findById(reservedId).orElseThrow(() -> new NoSuchElementException("Not Found"));
        return new StudentReserveDetailDto(
                reservedClass.getClassEntity().getTrainingProgram().getName(),
                reservedClass.getClassEntity().getStartDate(),
                reservedClass.getClassEntity().getEndDate(),
                reservedClass.getClassEntity().getId(),
                reservedClass.getClassEntity().getClassName(),
                reservedClass.getStudent().getId(),
                reservedClass.getStudent().getFullName(),
                reservedClass.getStudent().getEmail(),
                reservedClass.getStudent().getPhone(),
                reservedClass.getStartDate(),
                reservedClass.getEndDate(),
                reservedClass.getReasonReverse()
        );
    }

    @Override
    public Page<StudentByClassDto> searchStudentInClassSearchByFilter(int pageNumber, int pageSize, String sortField, String sortDir, String keyword, String[] filter, String[] viewBy, String classId) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        boolean fullName = false;
        boolean email = false;
        boolean phone = false;
        boolean dropout = false;
        boolean checkFinishedStatus = false;
        ClassEntity currenrClass = classRepository.findById(classId).orElseThrow(() -> new ClassEntityNotFoundException("not found "));
        checkFinishedStatus = currenrClass.getStatus().equalsIgnoreCase("finished");

        if (filter != null) {
            for (String filterString : filter) {
                if (filterString.equals("fullname")) fullName = true;
                if (filterString.equals("email")) email = true;
                if (filterString.equals("phone")) phone = true;
            }
        }
        if (viewBy != null) {
            for (String viewByString : viewBy) {
                if (viewByString.equals("dropout")) {
                    dropout = true;
                    break;
                }
            }
        }
        List<StudentByClassDto> resultList;
        if (keyword != null)
            resultList = findStudentByFIlter(keyword, fullName, email, phone, dropout, checkFinishedStatus, classId);
        else {
            resultList = findStudentByAttendingStatus(dropout, checkFinishedStatus, classId);
        }

        return getPage(resultList, pageNumber, pageSize, sort);
    }

    private Page<StudentByClassDto> getPage(List<StudentByClassDto> resultList, int pageNumber, int pageSize, Sort sort) {
        int start = Math.min((pageNumber - 1) * pageSize, resultList.size());
        int end = Math.min(start + pageSize, resultList.size());
        return new PageImpl<>(resultList.subList(start, end), PageRequest.of(pageNumber - 1, pageSize, sort), resultList.size());
    }

    @Override
    public List<Student> findByStatus(String status) {
        return studentRepository.findByStatus(status);
    }


    @Override
    public List<ClassEntityDTO> fetchRecommendClass(String classID, String sid) {

        var studentClass = studentClassRepository.findAllByStudent_Id(sid)
                .stream()
                .map(Student_Class::getClassId).toList();

        var reserveClass = reserveClassRepository.findAllByStudent_Id(sid)
                .stream()
                .map(Reserved_Class::getId).toList();

        var classData = classRepository.findById(classID).orElseThrow(
                () -> new NoSuchElementException("Class \"" + classID + "\" is not exist")
        );
        return classRepository.findAllByTrainingProgram_CodeAndStatusIgnoreCase(
                        classData.getTrainingProgram().getCode(),
                        "Planning")
                .stream()
                .filter(cls -> !studentClass.contains(cls.getId()))
                .map(ClassEntityMapper::mapToDTO)
                .toList();
    }

    @Override
    public List<String> getStudentEmails(List<String> studentIds) {
        List<Student> students = studentRepository.findByIdIn(studentIds);
        return students.stream().map(Student::getEmail).collect(Collectors.toList());

    }

}
