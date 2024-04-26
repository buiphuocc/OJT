package com.group1.FresherAcademyManagementSystem.repositories;

import com.group1.FresherAcademyManagementSystem.models.Student_Class;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface Student_ClassRepository extends JpaRepository<Student_Class, Long> {

    @Query(value="select * from student_class where student_id = :id", nativeQuery = true)
    Student_Class findByStudentId(@Param("id") String studentId);


}
