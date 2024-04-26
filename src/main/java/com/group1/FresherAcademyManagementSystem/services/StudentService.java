package com.group1.FresherAcademyManagementSystem.services;

import com.group1.FresherAcademyManagementSystem.dtos.*;
import com.group1.FresherAcademyManagementSystem.exceptions.StatusMismatchException;
import com.group1.FresherAcademyManagementSystem.models.ImportOption;
import com.group1.FresherAcademyManagementSystem.models.Student;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public interface StudentService {
    Page<Student> getStudentSearchByFilter(int pageNumber, int pageSize, String sortField, String sortDir, String keyword, String[] listFilter, String[] viewByStatus);

    Page<StudentByClassDto> searchStudentInClassSearchByFilter(int pageNumber, int pageSize, String sortField, String sortDir, String keyword, String[] listFilter, String[] viewByStatus, String classId);

    @Transactional(rollbackFor = {NoSuchElementException.class, StatusMismatchException.class})
    Map<String, Object> updateSelectedStudentStatus(String[] studentIds, String classId, String status);


    List<StudentListSystemDto> getAllStudent();

    List<Student> getAllStudentsByFilter(String keyword, String[] listFilter, String[] viewByStatus);

    List<StudentListSystemDto> getAllStudentWithReservation();

    void updateInfoStudentInClass(String classId, String studentId, Student_Detail_ClassDto2 studentDetailClassDto2);


    Student_Detail_ClassDto getStudent_detail(String classId, String studentId);

    void updateStatusStudentClass(String classId, String studentId, String status);

    void updateCertificateStatusStudentClass(String classId, String studentId, String status, LocalDate certificateDate);

    String toggleDropOutClass(String classId, String studentId);

    void deleteStudentById(String studentId);

    void deleteStudentByIdInClass(String studentId, String classId);

    StudentDto getStudentById(String studentId);

    StudentDto editStudentbyReserve(String studentId, StudentDto updatedStudentDetails);

    StudentCreateDto createStudent(StudentCreateDto studentCreateDto);

    String saveStudentsToDatabase(MultipartFile file, ImportOption importOption, String classId);

    List<StudentByClassDto> findByStudentByClass(String classId);


    List<ClassEntityDTO> fetchRecommendClass(String classID, String sid);

    List<String> getStudentEmails(List<String> studentIds);

    List<Student> findByStatus(String status);


    List<StudentByClassDto> getAllStudentsInClassByFilter(String value, String[] listFilter, String[] viewByStatus, String classId);

    StudentReserveDetailDto getStudentReserveDetail(Long reservedId);
}
