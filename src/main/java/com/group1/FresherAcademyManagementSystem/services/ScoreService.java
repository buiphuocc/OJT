package com.group1.FresherAcademyManagementSystem.services;

import com.group1.FresherAcademyManagementSystem.dtos.Class_Score_DetailDto;
import com.group1.FresherAcademyManagementSystem.dtos.Class_Score_DetailDto2;
import com.group1.FresherAcademyManagementSystem.dtos.ScoreDto2;
import com.group1.FresherAcademyManagementSystem.models.Score;
import org.springframework.web.multipart.MultipartFile;
import com.group1.FresherAcademyManagementSystem.models.*;
import java.io.InputStream;
import java.util.List;

public interface ScoreService {
    Class_Score_DetailDto getScoreDetail(String studentId, String classId);
    List<Class_Score_DetailDto2> viewScoreDetail(String classId);

    void updateScocesStudentDetailInClass(List<ScoreDto2> scoreIds);

    String saveScoresToDatabase(MultipartFile file);

    boolean isValidExcelFile(MultipartFile file);

    void writeScoresToExcel(List<Score> scores,String filePath,String classId);

    List<Score> getAllScores();

    List<String> getStudentIdsOfClass(String classId);



}
