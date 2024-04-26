package com.group1.FresherAcademyManagementSystem.mappers;

import com.group1.FresherAcademyManagementSystem.dtos.StudentDto;
import com.group1.FresherAcademyManagementSystem.dtos.StudentDto2;
import com.group1.FresherAcademyManagementSystem.models.Student;
import java.util.stream.Collectors;

public class StudentMapper {
    public static StudentDto2 maptoDto2(Student student) {
        return StudentDto2.builder()
                 .id(student.getId())
                 .fullName(student.getFullName())
                 .dob(student.getDob())
                 .gender(student.getGender())
                 .phone(student.getPhone())
                 .email(student.getEmail())
                 .school(student.getSchool())
                 .major(student.getMajor())
                 .graduatedDate(student.getGraduatedDate())
                 .FAAccount(student.getFAAccount())
                 .status(student.getStatus())
                 .joinDate(student.getJoinDate())
                 .gpa(student.getGpa())
                 .address(student.getAddress())
                 .type(student.getType())
                 .RECer(student.getRECer())
                 .area(student.getArea())
                 .scores(student.getScores().stream().map(ScoreMapper::mapToDto).collect(Collectors.toList()))
//                 .student_classes(student.getStudent_classes())
                .build();
    }

    public static Student mapToEntity2(StudentDto2 studentDto) {
        return Student.builder()
                .id(studentDto.getId())
                .fullName(studentDto.getFullName())
                .dob(studentDto.getDob())
                .gender(studentDto.getGender())
                .phone(studentDto.getPhone())
                .email(studentDto.getEmail())
                .school(studentDto.getSchool())
                .major(studentDto.getMajor())
                .graduatedDate(studentDto.getGraduatedDate())
                .FAAccount(studentDto.getFAAccount())
                .status(studentDto.getStatus())
                .joinDate(studentDto.getJoinDate())
                .gpa(studentDto.getGpa())
                .address(studentDto.getAddress())
                .type(studentDto.getType())
                .RECer(studentDto.getRECer())
                .area(studentDto.getArea())
                .scores(studentDto.getScores().stream().map(ScoreMapper::mapToEntity).collect(Collectors.toList()))
//                 .studentDto_classes(student.getStudent_classes())
                .build();
    }
    public static StudentDto mapToDto(Student student) {
        return new StudentDto(
                student.getId(),
                student.getFullName(),
                student.getDob(),
                student.getGender(),
                student.getPhone(),
                student.getEmail(),
                student.getSchool(),
                student.getMajor(),
                student.getGraduatedDate(),
                student.getGpa(),
                student.getFAAccount(),
                student.getAddress(),
                student.getType(),
                student.getStatus(),
                student.getRECer(),
                student.getJoinDate(),
                student.getArea(),
                student.getEmailSend_students(),
                student.getStudent_modules(),
                student.getScores(),
                student.getReserved_classes(),
                student.getStudentClasses()
        );
    }

    public static Student mapToEntity(StudentDto studentDto){
        return new Student(
                studentDto.getId(),
                studentDto.getFullName(),
                studentDto.getDob(),
                studentDto.getGender(),
                studentDto.getPhone(),
                studentDto.getEmail(),
                studentDto.getSchool(),
                studentDto.getMajor(),
                studentDto.getGraduatedDate(),
                studentDto.getGpa(),
                studentDto.getFAAccount(),
                studentDto.getAddress(),
                studentDto.getType(),
                studentDto.getStatus(),
                studentDto.getRECer(),
                studentDto.getJoinDate(),
                studentDto.getArea(),
                studentDto.getEmailSend_students(),
                studentDto.getStudent_modules(),
                studentDto.getScores(),
                studentDto.getReserved_classes(),
                studentDto.getStudent_classes()
        );
    }

}
