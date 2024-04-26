package com.group1.FresherAcademyManagementSystem.dtos;

import com.group1.FresherAcademyManagementSystem.models.ClassEntity;
import com.group1.FresherAcademyManagementSystem.models.Student;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class reserveClassDTO {
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reasonReverse;
    private ClassEntity classEntity;
    private Student student;

    public reserveClassDTO(Long id, LocalDate startDate, LocalDate endDate, String classId, String studentid, String reasonReverse) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reasonReverse = reasonReverse;

        // Tạo một đối tượng ClassEntity với classId được truyền vào
        this.classEntity = new ClassEntity();
        this.classEntity.setId(classId);

        this.student = new Student();
        this.student.setId(studentid);
    }
}
