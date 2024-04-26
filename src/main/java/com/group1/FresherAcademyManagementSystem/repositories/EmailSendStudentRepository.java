package com.group1.FresherAcademyManagementSystem.repositories;

import com.group1.FresherAcademyManagementSystem.models.EmailSend_Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailSendStudentRepository extends JpaRepository<EmailSend_Student, Long> {
}
