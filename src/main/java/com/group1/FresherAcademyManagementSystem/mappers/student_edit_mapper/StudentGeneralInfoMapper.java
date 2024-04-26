package com.group1.FresherAcademyManagementSystem.mappers.student_edit_mapper;

import com.group1.FresherAcademyManagementSystem.dtos.student_edit_dto.StudentGeneralInfoDTO;
import com.group1.FresherAcademyManagementSystem.models.Student;

public class StudentGeneralInfoMapper {
    public static Student patchEntity(Student student, StudentGeneralInfoDTO generalInfoDTO) {
        student.setFullName(generalInfoDTO.getFullName());
        student.setGender(generalInfoDTO.getGender());
        student.setDob(generalInfoDTO.getDob());
        student.setStatus(generalInfoDTO.getStatus());
        student.setPhone(generalInfoDTO.getPhone());
        student.setEmail(generalInfoDTO.getEmail());
        student.setAddress(generalInfoDTO.getAddress());
        student.setArea(generalInfoDTO.getArea());
        return student;
    }

    public static StudentGeneralInfoDTO mapToDTO(Student student) {
        return new StudentGeneralInfoDTO(
                student.getId(),
                student.getFullName(),
                student.getGender(),
                student.getDob(),
                student.getStatus(),
                student.getPhone(),
                student.getEmail(),
                student.getAddress(),
                student.getArea()
        );
    }
}
