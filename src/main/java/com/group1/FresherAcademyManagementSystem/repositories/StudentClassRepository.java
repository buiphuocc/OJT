package com.group1.FresherAcademyManagementSystem.repositories;

import com.group1.FresherAcademyManagementSystem.models.Student_Class;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentClassRepository extends JpaRepository<Student_Class, Long> {
    Optional<Student_Class> findByStudentIdAndClassEntityId(String studentId, String classId);

    Optional<Student_Class> findTopByClassEntityId(String cid);

    boolean existsByStudent_IdAndAndAttendingStatus(String sid, String att_stt);

    @Query(value = "SELECT COUNT(*) = 0 FROM student_class", nativeQuery = true)
    boolean isEmpty();

    boolean existsByStudentIdAndClassEntityId(String studentId, String classId);

    List<Student_Class> findAllByStudentIdAndClassEntity_StatusIgnoreCase(String studentId, String attending_status);

    List<Student_Class> findAllByStudent_Id(String id);

    boolean existsByStudent_Id(String id);

    List<Student_Class> findAllByClassEntity_Id(String id);

    @Query(value = "SELECT *  FROM student_class where attending_status = 'Reservation' and student_id = :studentID", nativeQuery = true)
    List<Student_Class> findAllByStudentIdWithReservation(@Param("studentID") String id);

    boolean existsByClassEntity_IdAndStudent_Id(String classID, String studentID);

    @Query(value = "SELECT * FROM student_class sc where sc.student_id = :studentId and sc.class_id= :classId", nativeQuery = true)
    List<Student_Class> findByStudentIdAndClassId(@Param("studentId") String studentId, @Param("classId") String classId);

    @Query(value = "SELECT *  FROM student_class where attending_status = 'Reservation'", nativeQuery = true)
    List<Student_Class> findAllByStatusReservation();


    Student_Class findByStudentId(String studentId);

    List<Student_Class> findByClassEntityId(String classId);

    @Query(value = "SELECT MAX(sc.id) FROM Student_Class sc")
    Long findMaxId();

}

