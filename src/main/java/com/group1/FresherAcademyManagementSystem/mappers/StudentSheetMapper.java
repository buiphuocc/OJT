package com.group1.FresherAcademyManagementSystem.mappers;

import com.group1.FresherAcademyManagementSystem.dtos.StudentSheetDTO;
import com.group1.FresherAcademyManagementSystem.models.Student;

public class StudentSheetMapper {
    public static StudentSheetDTO mapToDTO(Student student){
        return  new StudentSheetDTO(
                student.getFullName(),
                student.getDob(),
                student.getGender(),
                student.getPhone(),
                student.getEmail(),
                student.getSchool(),
                student.getMajor(),
                student.getGraduatedDate(),
                student.getGpa(),
                student.getAddress(),
                student.getFAAccount(),
                student.getType(),
                student.getStatus(),
                student.getRECer(),
                student.getArea()
        );
    }
}
