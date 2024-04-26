package com.group1.FresherAcademyManagementSystem.repositories;

import com.group1.FresherAcademyManagementSystem.models.Student_Class;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassStudentRepository extends JpaRepository<Student_Class, Long> {

    @Query(value = "SELECT * FROM student_class where student_id = :studentId AND class_id = :classId", nativeQuery = true)
    Student_Class findByStudentIdAndClassId(String studentId, String classId);

    @Query(value = "SELECT * FROM student_class ORDER BY id ASC ", nativeQuery = true)
    List<Student_Class> findAll();
}
