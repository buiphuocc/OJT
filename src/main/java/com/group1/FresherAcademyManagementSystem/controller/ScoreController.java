package com.group1.FresherAcademyManagementSystem.controller;

import com.group1.FresherAcademyManagementSystem.dtos.Class_Score_DetailDto;
import com.group1.FresherAcademyManagementSystem.dtos.Class_Score_DetailDto2;
//import com.group1.FresherAcademyManagementSystem.exceptions.CustomExceptionHandler;
import com.group1.FresherAcademyManagementSystem.dtos.ScoreDto2;
import com.group1.FresherAcademyManagementSystem.exceptions.CustomSuccessHandler;
import com.group1.FresherAcademyManagementSystem.models.Score;
import com.group1.FresherAcademyManagementSystem.services.ScoreService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/trainer")
@CrossOrigin("http://localhost:5173/")
public class ScoreController {

    private final ScoreService scoreService;

    private final String tempDirectoryPath = "D:"; // Thư mục tạm thời

    @Autowired
    public ScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @GetMapping("/scores/{studentId}/{classId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TRAINER')")
    public ResponseEntity<Object> getScore(@PathVariable("studentId") String studentId, @PathVariable("classId") String classId) {
        Class_Score_DetailDto scoreDto = scoreService.getScoreDetail(studentId, classId);
        return CustomSuccessHandler.responseBuilder("View success!", HttpStatus.OK, scoreDto);
    }

    @GetMapping("/trainee/scores/{classId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TRAINER')")
    public ResponseEntity<Object> viewScoreDetail(@PathVariable("classId") @NotBlank String classId) {
        List<Class_Score_DetailDto2> scoreDto = scoreService.viewScoreDetail(classId);
        return CustomSuccessHandler.responseBuilder("View success!", HttpStatus.OK, scoreDto);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('TRAINER')")
    @PutMapping("update-scores-student-detail")
    public ResponseEntity<Object> updateScoresStudentDetail(@RequestBody @Valid List<ScoreDto2> scores) {
        scoreService.updateScocesStudentDetailInClass(scores);
        return CustomSuccessHandler.responseBuilder("Update success!", HttpStatus.OK, "");
    }
    @PreAuthorize("hasRole('ADMIN') or hasRole('TRAINER')")
    @PostMapping("/upload-score")
    public ResponseEntity<?> uploadScoreData(@RequestParam("file") MultipartFile file) {
        try {
            this.scoreService.saveScoresToDatabase(file);
            return CustomSuccessHandler.responseBuilder("EM4 The File import process has been completed.", HttpStatus.OK, null);
        } catch (UnsupportedOperationException e) {
            return CustomSuccessHandler.responseBuilder(
                    e.getMessage(),
                    HttpStatus.BAD_REQUEST,
                    null
            );
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('TRAINER')")
    @GetMapping("/export-scores/{classId}")
    public ResponseEntity<?> exportScoresToExcel(@PathVariable("classId")String classId) {
        List<Score> scores = scoreService.getAllScores();
        String fileName = "Scores" + ".xlsx";
        String filePath = tempDirectoryPath + File.separator + fileName;
        scoreService.writeScoresToExcel(scores, filePath,classId); // Gọi phương thức từ ScoreServiceImpl
        try {
            this.scoreService.writeScoresToExcel(scores, filePath,classId);
            return CustomSuccessHandler.responseBuilder("Export file Score's Excel successfully", HttpStatus.OK, null);
        } catch (NullPointerException e) {
            return CustomSuccessHandler.responseBuilder(
                    e.getMessage(),
                    HttpStatus.BAD_REQUEST, null
            );
        }
    }

}
