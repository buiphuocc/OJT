package com.group1.FresherAcademyManagementSystem.repositories;

import com.group1.FresherAcademyManagementSystem.models.EmailTemplate;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, Long> {
    @Query("SELECT e FROM EmailTemplate e WHERE e.type IN :types AND lower(e.name) LIKE lower(concat('%', :keyword, '%'))")
    Page<EmailTemplate> findByType(@Param("keyword") String keyword, @Param("types") String[] types, Pageable pageable);

    @Query("SELECT e FROM EmailTemplate e")
    @NonNull
    List<EmailTemplate> findAllEmailTemplate(@NotNull Pageable pageable);


//    @Query("SELECT et FROM EmailTemplate et " +
//            "WHERE " +
//            "(:keywordExisted = TRUE AND ((et.id = :keyword) OR" +
//            "(et.name LIKE lower(concat('%', :keyword, '%'))))) AND" +
//            "((:student = TRUE AND lower(et.type) = 'student') OR" +
//            "(:trainer = TRUE AND lower(et.type) = 'trainer')) AND" +
//            "((:inform = TRUE AND lower(et.category) = 'inform') OR" +
//            "(:remind = TRUE AND lower(et.category) = 'remind') OR" +
//            "(:score = TRUE AND lower(et.category) = 'score') OR" +
//            "(:reservation = TRUE AND lower(et.category) = 'reservation'))")

    @Query("SELECT et FROM EmailTemplate et \n" +
            "WHERE \n" +
            "  (:keywordExisted = TRUE AND lower(et.name) LIKE lower(concat('%', :keyword, '%')))  \n" +
            "  OR \n" +
            "  ((:student = TRUE AND lower(et.type) = 'student') OR \n" +
            "   (:trainer = TRUE AND lower(et.type) = 'trainer'))\n" +
            "  AND \n" +
            "  ((:inform = TRUE AND lower(et.category) = 'inform') OR \n" +
            "   (:remind = TRUE AND lower(et.category) = 'remind') OR \n" +
            "   (:score = TRUE AND lower(et.category) = 'score') OR \n" +
            "   (:reservation = TRUE AND lower(et.category) = 'reservation'))")
    List<EmailTemplate> findTypeAndCategory(Pageable pageable, String keyword, boolean keywordExisted, boolean student, boolean trainer, boolean inform, boolean remind, boolean score, boolean reservation);

    @Query("SELECT et FROM EmailTemplate et \n" +
            "WHERE \n" +
            "  (:keywordExisted = TRUE AND lower(et.name) LIKE lower(concat('%', :keyword, '%')))  \n" +
            "  AND \n" +
            "  ((:student = TRUE AND lower(et.type) = 'student') OR \n" +
            "   (:trainer = TRUE AND lower(et.type) = 'trainer'))\n" +
            "  AND \n" +
            "  ((:inform = TRUE AND lower(et.category) = 'inform') OR \n" +
            "   (:remind = TRUE AND lower(et.category) = 'remind') OR \n" +
            "   (:score = TRUE AND lower(et.category) = 'score') OR \n" +
            "   (:reservation = TRUE AND lower(et.category) = 'reservation'))")
    List<EmailTemplate> findByNameAndTypeAndCategory(Pageable pageable, String keyword, boolean keywordExisted, boolean student, boolean trainer, boolean inform, boolean remind, boolean score, boolean reservation);

    @Query("SELECT et FROM EmailTemplate et \n" +
            "WHERE \n" +
            "  ((:student = TRUE AND lower(et.type) = 'student') OR \n" +
            "   (:trainer = TRUE AND lower(et.type) = 'trainer'))\n")
    List<EmailTemplate> findTypeOrCategory1(Pageable pageable, boolean student, boolean trainer);

    @Query("SELECT et FROM EmailTemplate et \n" +
            "WHERE \n" +
            "  (:keywordExisted = TRUE AND lower(et.name) LIKE lower(concat('%', :keyword, '%')))  \n" +
            "  AND \n" +
            "  ((:student = TRUE AND lower(et.type) = 'student') OR \n"+
            "   (:trainer = TRUE AND lower(et.type) = 'trainer'))\n")
    List<EmailTemplate> findTypeOrCategory2(Pageable pageable, boolean student, boolean trainer, String keyword, boolean keywordExisted);

    @Query("SELECT et FROM EmailTemplate et \n" +
            "WHERE \n" +
            "  ((:inform = TRUE AND lower(et.category) = 'inform') OR \n" +
            "   (:remind = TRUE AND lower(et.category) = 'remind') OR \n" +
            "   (:score = TRUE AND lower(et.category) = 'score') OR \n" +
            "   (:reservation = TRUE AND lower(et.category) = 'reservation'))")
    List<EmailTemplate> findTypeOrCategory3(Pageable pageable, boolean inform, boolean remind, boolean score, boolean reservation);

    @Query("SELECT et FROM EmailTemplate et \n" +
            "WHERE \n" +
            "  (:keywordExisted = TRUE AND lower(et.name) LIKE lower(concat('%', :keyword, '%')))  \n" +
            "  AND \n" +
            "  ((:inform = TRUE AND lower(et.category) = 'inform') OR \n" +
            "   (:remind = TRUE AND lower(et.category) = 'remind') OR \n" +
            "   (:score = TRUE AND lower(et.category) = 'score') OR \n" +
            "   (:reservation = TRUE AND lower(et.category) = 'reservation'))")
    List<EmailTemplate> findTypeOrCategory4(Pageable pageable, String keyword, boolean keywordExisted,   boolean inform, boolean remind, boolean score, boolean reservation);

    @Query("SELECT et FROM EmailTemplate et \n" +
            "WHERE \n" +
            "  (:keywordExisted = TRUE AND lower(et.name) LIKE lower(concat('%', :keyword, '%')))")
    List<EmailTemplate> findByName(Pageable pageable, String keyword, boolean keywordExisted);

    EmailTemplate findByName(String name);
}
