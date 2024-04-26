package com.group1.FresherAcademyManagementSystem.repositories;

import com.group1.FresherAcademyManagementSystem.models.ClassEntity;
import com.group1.FresherAcademyManagementSystem.models.Reserved_Class;
import com.group1.FresherAcademyManagementSystem.models.Student_Class;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ReserveClassRepository extends JpaRepository<Reserved_Class, Long> {
    List<Reserved_Class> findAllByStudent_Id(String id);

    boolean existsByClassEntity_IdAndStudent_Id(String classId, String studentID);

    int countAllByStudent_Id(String id);

    @Query("SELECT rc FROM Reserved_Class rc join ClassEntity c on rc.classEntity = c join Student s on rc.student = s WHERE " +
            "((:fullName = true AND lower(s.fullName) LIKE lower(concat('%', :keyword, '%'))) OR " +
            "(:id = true AND CAST(rc.id AS string) LIKE lower(concat('%', :keyword, '%'))) OR" +
            "(:studentId = true AND lower(s.id) LIKE lower(concat('%', :keyword, '%'))) OR" +
            "(:classId = true AND lower(c.id) LIKE lower(concat('%', :keyword, '%'))))")
    Page<Reserved_Class> findByIdOrClassNameOrStatus(String keyword, Pageable pageable, boolean id, boolean fullName, boolean studentId, boolean classId);

    @Query("SELECT rc FROM Reserved_Class rc join ClassEntity c on rc.classEntity = c WHERE " +
            "(:inprogress = true AND lower(c.status) = 'inprogress') OR " +
            "(:finished = true AND lower(c.status) = 'finished')")
    Page<Reserved_Class> findByStatus(Pageable pageable, boolean inprogress, boolean finished);

    @Query("SELECT rc FROM Reserved_Class rc join ClassEntity c on rc.classEntity = c")
    @NonNull
    Page<Reserved_Class> findAll(@NonNull Pageable pageable);

    @Query("SELECT rc FROM Reserved_Class rc WHERE rc.student = :studentId")
    List<Reserved_Class> findByStudent_Id(String studentId);

    @Query(value = "SELECT * FROM reserved_class where student_id = :studentId AND class_id = :classId", nativeQuery = true)
    Reserved_Class findByStudentIdAndClassId(String studentId, String classId);
    @Query("SELECT rc FROM Reserved_Class rc")
    List<Reserved_Class> findAllReservedClasses();
}
