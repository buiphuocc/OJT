package com.group1.FresherAcademyManagementSystem.repositories;

import com.group1.FresherAcademyManagementSystem.models.ClassEntity;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRepository extends JpaRepository<ClassEntity, String> {

    List<ClassEntity> findAllByStatusIgnoreCase(String status);

    @Query("SELECT c FROM ClassEntity c WHERE " +
            "lower(c.status) = 'planning'")
    @NonNull
    Page<ClassEntity> findAll(@NonNull Pageable pageable);

    @Query("SELECT c FROM ClassEntity c WHERE " +
            "(:inprogress = true AND lower(c.status) = 'inprogress') OR " +
            "(:finished = true AND lower(c.status) = 'finished') OR " +
            "lower(c.status) = 'planning'")
    Page<ClassEntity> findByStatus(Pageable pageable, boolean inprogress, boolean finished);

    @Query("SELECT c FROM ClassEntity c WHERE " +
            "(lower(c.className) LIKE lower(concat('%', :keyword, '%')) OR " +
            "lower(c.id) LIKE lower(concat('%', :keyword, '%'))) AND " +
            "((:inprogress = true AND lower(c.status) = 'inprogress') OR " +
            "(:finished = true AND lower(c.status) = 'finished') OR " +
            "(lower(c.status) = 'planning'))")
    Page<ClassEntity> findByIdOrClassNameOrStatus(String keyword, Pageable pageable, boolean inprogress, boolean finished);

    @Query("SELECT class FROM ClassEntity class WHERE "
            + "class.id LIKE :pattern AND "
            + "class.status IN ('Active','Planning') AND "
            + "class.startDate >= CURRENT_DATE")
    List<ClassEntity> findAlikeSubject(String pattern);

    List<ClassEntity> findAllByTrainingProgram_CodeAndStatusIgnoreCase(
            String trainingCode,
            String status);
}
