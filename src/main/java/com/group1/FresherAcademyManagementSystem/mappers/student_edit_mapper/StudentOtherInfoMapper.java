package com.group1.FresherAcademyManagementSystem.mappers.student_edit_mapper;

import com.group1.FresherAcademyManagementSystem.dtos.student_edit_dto.StudentOtherInfoDTO;
import com.group1.FresherAcademyManagementSystem.models.Student;

public class StudentOtherInfoMapper {
    public static Student patchEntity(Student student, StudentOtherInfoDTO otherInfoDTO) {
        student.setSchool(otherInfoDTO.getSchool());
        student.setMajor(otherInfoDTO.getMajor());
        student.setRECer(otherInfoDTO.getRecer());
        student.setGpa(otherInfoDTO.getGpa());
        student.setGraduatedDate(otherInfoDTO.getYearOfGraduation());
        return student;
    }

    public static StudentOtherInfoDTO mapToDTO(Student student) {
        return new StudentOtherInfoDTO(
                student.getSchool(),
                student.getMajor(),
                student.getRECer(),
                student.getGpa(),
                student.getGraduatedDate());
    }
}
