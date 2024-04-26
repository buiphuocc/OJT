package com.group1.FresherAcademyManagementSystem.repositories;

import com.group1.FresherAcademyManagementSystem.models.Student;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {

    boolean existsById(String id);

    @Query(nativeQuery = true, value = "SELECT * FROM student s WHERE id IN " +
            "(SELECT student_id FROM student_class WHERE class_id = :classId)")
    List<Student> findByStudentClasses_ClassEntityClassId(@Param("classId") String classId);

    @Query("SELECT s.id FROM Student s")
    List<String> getAllIds();

    @Query("SELECT s FROM Student s WHERE " +
            "((:fullName = TRUE AND lower(s.fullName) LIKE lower(concat('%', :keyword, '%'))) OR " +
            "(:email = TRUE AND lower(s.email) LIKE lower(concat('%', :keyword, '%'))) OR " +
            "(:phone = TRUE AND lower(s.phone) LIKE lower(concat('%', :keyword, '%'))) OR " +
            "lower(s.id) LIKE lower(concat('%', :keyword, '%'))) AND " +
            "((:inactive = true AND lower(s.status) = 'inactive') OR " +
            "(:disable = true AND lower(s.status) = 'disable') OR " +
            "(lower(s.status) = 'active'))")
    Page<Student> findByIdOrFullNameOrEmailOrPhoneOrStatus(String keyword, Pageable pageable, boolean fullName, boolean email, boolean phone, boolean inactive, boolean disable);


    @Query("SELECT s FROM Student s WHERE " +
            "lower(s.status) = 'active'")
    @NonNull
    Page<Student> findAll(@NonNull Pageable pageable);

    @Query("SELECT s FROM Student s WHERE " +
            "(:inactive = true AND lower(s.status) = 'inactive') OR " +
            "(:disable = true AND lower(s.status) = 'disable') OR " +
            "lower(s.status) = 'active'")
    Page<Student> findByStatus(Pageable pageable, boolean inactive, boolean disable);

    List<Student> findByIdIn(List<String> studentIds);

    @Query(nativeQuery = true, value = "SELECT s.* FROM student s " +
            "INNER JOIN student_class sc ON s.id = sc.student_id " +
            "WHERE sc.class_id = :classId")
    Page<Student> findByStudentClasses_ClassEntityClassId(@Param("classId") String classId, Pageable pageable);

    Optional<Student> findTopByOrderByIdDesc();

    List<Student> findByStatus(String status);

    @Transactional
    @Modifying
    @Query(value = "update student set status = :status where id = :studentId ", nativeQuery = true)
    void updateStudentStatus(@Param("studentId") String studentId, @Param("status") String status);

    @Query("SELECT s FROM Student s WHERE " +
            "((:fullName = TRUE AND lower(s.fullName) LIKE lower(concat('%', :keyword, '%'))) OR " +
            "(:email = TRUE AND lower(s.email) LIKE lower(concat('%', :keyword, '%'))) OR " +
            "(:phone = TRUE AND lower(s.phone) LIKE lower(concat('%', :keyword, '%'))) OR " +
            "lower(s.id) LIKE lower(concat('%', :keyword, '%'))) AND " +
            "((:inactive = true AND lower(s.status) = 'inactive') OR " +
            "(:disable = true AND lower(s.status) = 'disable') OR " +
            "(lower(s.status) = 'active'))")
    List<Student> findByIdOrFullNameOrEmailOrPhoneOrStatusForExport(String keyword, boolean fullName, boolean email, boolean phone, boolean inactive, boolean disable);

    @Query("SELECT s FROM Student s WHERE " +
            "lower(s.status) = 'active'")
    List<Student> findAllForExport();

    @Query("SELECT s FROM Student s WHERE " +
            "(:inactive = true AND lower(s.status) = 'inactive') OR " +
            "(:disable = true AND lower(s.status) = 'disable') OR " +
            "lower(s.status) = 'active'")
    List<Student> findByStatusForExport(boolean inactive, boolean disable);

    @Query("SELECT sc.student FROM Student_Class sc " +
            "JOIN sc.student s " +
            "WHERE " +
            "((:fullName = TRUE AND lower(s.fullName) LIKE lower(concat('%', :keyword, '%'))) OR " +
            "(:email = TRUE AND lower(s.email) LIKE lower(concat('%', :keyword, '%'))) OR " +
            "(:phone = TRUE AND lower(s.phone) LIKE lower(concat('%', :keyword, '%'))) OR " +
            "lower(s.id) LIKE lower(concat('%', :keyword, '%'))) AND " +
            "(sc.attendingStatus = 'inclass' OR " +
            "(:dropout = TRUE AND sc.attendingStatus = 'dropout') OR" +
            "(:checkFinishStatus = TRUE AND sc.attendingStatus = 'finished')) AND " +
            "sc.classEntity.id = :classId")
    List<Student> findByIdOrFullNameOrEmailOrPhoneOrStatusAndClassId(String keyword, boolean fullName, boolean email, boolean phone, boolean dropout, boolean checkFinishStatus, String classId);

    @Query("SELECT sc.student FROM Student_Class sc " +
            "WHERE " +
            "(sc.attendingStatus = 'inclass' OR " +
            "(:dropout = TRUE AND sc.attendingStatus = 'dropout') OR" +
            "(:checkFinishStatus = TRUE AND sc.attendingStatus = 'finished')) AND " +
            "lower(sc.classEntity.id) LIKE lower(concat('%', :classId, '%'))")
    List<Student> findByStatusAndClassId(boolean dropout, boolean checkFinishStatus, String classId);
}
