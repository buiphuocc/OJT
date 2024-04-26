package com.group1.FresherAcademyManagementSystem.mappers.student_edit_mapper;

import com.group1.FresherAcademyManagementSystem.dtos.student_edit_dto.StudentViewEditDTO;
import com.group1.FresherAcademyManagementSystem.mappers.ClassEntityMapper;
import com.group1.FresherAcademyManagementSystem.models.Student;
import com.group1.FresherAcademyManagementSystem.models.Student_Class;

import java.util.stream.Collectors;

public class StudentViewEditMapper {
    public static StudentViewEditDTO entityToDTO(Student student) {
        return new StudentViewEditDTO(
                StudentGeneralInfoMapper.mapToDTO(student),
                StudentOtherInfoMapper.mapToDTO(student),
                student.getReserved_classes().stream()
                        .map(ReservedClassMapper::mapToDTO)
                        .toList(),
                student.getStudentClasses().stream()
                        .map(Student_Class::getClassEntity)
                        .map(ClassEntityMapper::mapToDTO)
                        .toList());
    }
}
