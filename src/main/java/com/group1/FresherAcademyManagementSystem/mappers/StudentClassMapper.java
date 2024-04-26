package com.group1.FresherAcademyManagementSystem.mappers;

import com.group1.FresherAcademyManagementSystem.dtos.ClassEntityDTO;
import com.group1.FresherAcademyManagementSystem.dtos.StudentClassDTO;
import com.group1.FresherAcademyManagementSystem.dtos.student_edit_dto.StudentClassEditDTO;
import com.group1.FresherAcademyManagementSystem.models.ClassEntity;
import com.group1.FresherAcademyManagementSystem.models.Student;
import com.group1.FresherAcademyManagementSystem.models.Student_Class;

public class StudentClassMapper {
    public static StudentClassDTO mapToDTO(Student_Class studentClass) {
        return new StudentClassDTO(
                studentClass.getId(),
                studentClass.getAttendingStatus(),
                studentClass.getResult(),
                studentClass.getFinalScore(),
                studentClass.getGpaLevel(),
                studentClass.getCertificationStatus(),
                studentClass.getCertificationDate(),
                studentClass.getMethod(),
                studentClass.getClassEntity().getId(),
                studentClass.getStudent().getId()
        );
    }

    public static Student_Class mapToEntity(String id, StudentClassEditDTO studentClassDTO) {
        var classEntity = new ClassEntity();
        classEntity.setId(studentClassDTO.getClassID());
        var student = new Student();
        student.setId(id);
        return new Student_Class(
                null,
                "In class",
                null,
                null,
                null,
                null,
                null,
                studentClassDTO.getMethod(),
                classEntity,
                student
        );
    }

    public static Student_Class mapToEntity(StudentClassDTO studentClassDTO) {
        var classEntity = new ClassEntity();
        classEntity.setId(studentClassDTO.getClassID());
        var student = new Student();
        student.setId(studentClassDTO.getStudentID());
        return new Student_Class(
                studentClassDTO.getId(),
                studentClassDTO.getAttendingStatus(),
                studentClassDTO.getResult(),
                studentClassDTO.getFinalScore(),
                studentClassDTO.getGpaLevel(),
                studentClassDTO.getCertificationStatus(),
                studentClassDTO.getCertificationDate(),
                studentClassDTO.getMethod(),
                classEntity,
                student
        );
    }

    public static ClassEntityDTO mapToClassDTO(Student_Class studentClass) {
        return new ClassEntityDTO(
                studentClass.getClassEntity().getId(),
                studentClass.getClassEntity().getClassName(),
                studentClass.getClassEntity().getStartDate(),
                studentClass.getClassEntity().getEndDate(),
                studentClass.getAttendingStatus()
        );
    }
}
