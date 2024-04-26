package com.group1.FresherAcademyManagementSystem.services;

import com.group1.FresherAcademyManagementSystem.dtos.ClassEntityDTO;
import com.group1.FresherAcademyManagementSystem.dtos.student_edit_dto.*;
import jakarta.transaction.Transactional;

import java.util.List;

public interface StudentEditService {
    StudentGeneralInfoDTO updateStudentGeneralInfo(String id, StudentGeneralInfoDTO info);

    StudentOtherInfoDTO updateStudentOtherInfo(String id, StudentOtherInfoDTO info);

    @Transactional
    List<ClassEntityDTO> addClassEntity(String id, String classID);

    List<ClassEntityDTO> fetchEnrolledClass(String id);

    List<ReservedClassDTO> addReservedClass(String id, ReservedClassDTO reservedClass);

    List<ReservedClassViewDTO> fetchReservableClass(String id);

    List<ReservedClassDTO> fetchReservedClass(String id);

    StudentViewEditDTO fetchStudentInfo(String id);

    List<ClassEntityDTO> fetchEnrollableClass(String id);

    Boolean addReservedClassFromList(String id, ReservedClassDTO reservedClass);

}
