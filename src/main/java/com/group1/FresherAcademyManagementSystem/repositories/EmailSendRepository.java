package com.group1.FresherAcademyManagementSystem.repositories;

import com.group1.FresherAcademyManagementSystem.models.EmailSend;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmailSendRepository extends JpaRepository<EmailSend, Long> {
    @Query(value = "SELECT * FROM email_send es", nativeQuery = true)
    Page<EmailSend> findAllEmailHistory(Pageable pageable);

    @Query(value = "SELECT es.* FROM email_send es INNER JOIN email_template et ON es.template_id = et.id WHERE et.category='Reservation'", nativeQuery = true)
    Page<EmailSend> findAllEmailHistoryWithReservationCategory(Pageable pageable);

    @Query(value = "SELECT es.* FROM email_send es JOIN email_send_student ess ON ess.email_id = es.id WHERE ess.receiver_id = :receiverId", nativeQuery = true)
    List<EmailSend> getEmailSentToStudentLog(String receiverId);
}
