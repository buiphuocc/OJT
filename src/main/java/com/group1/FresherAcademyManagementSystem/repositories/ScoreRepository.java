package com.group1.FresherAcademyManagementSystem.repositories;

import com.group1.FresherAcademyManagementSystem.models.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {
    @Query(value = "select * from score where student_id = :studentId", nativeQuery = true)
    List<Score> findAllByStudentId(String studentId);

    Boolean existsByAssignmentIdAndStudentId(Long aid, String sid);

    List<Score> findAllByStudent_IdAndAssignmentIdIn(String sid, List<Long> aids);

    Optional<Score> findTopByAssignmentId(Long aLong);

    List<Score> findAllByStudentIdAndAssignmentIdIn(String sid, List<Long> aids);
}
