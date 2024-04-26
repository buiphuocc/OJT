package com.group1.FresherAcademyManagementSystem.services;


import com.group1.FresherAcademyManagementSystem.dtos.ClassEntityDTO;
import com.group1.FresherAcademyManagementSystem.exceptions.StatusMismatchException;
import com.group1.FresherAcademyManagementSystem.repositories.StudentClassRepository;
import com.group1.FresherAcademyManagementSystem.services.Impl.StudentServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@Execution(ExecutionMode.CONCURRENT)
@Sql({"/db/student.sql",
        "/db/training_program.sql",
        "/db/class_entity.sql",
        "/db/student_class.sql"})
public class StudentServiceTest {

    @Autowired
    StudentServiceImpl studentService;

    @Autowired
    StudentClassRepository studentClassRepository;

    @Test()
    void emptyIDsEditBatch() {
        String[] emptyIDs = new String[1];
        assertThrows(NoSuchElementException.class, () -> {
            studentService.updateSelectedStudentStatus(emptyIDs, "", "");
        });
    }


    @Test()
    void oneIDEmptyClassEditBatch() {
        String[] emptyIDs = new String[1];
        emptyIDs[0] = "4479badb-de18-4316-a529-996bf567518e";
        var classID = "";

        assertThrows(NoSuchElementException.class, () -> {
            var response = studentService.updateSelectedStudentStatus(emptyIDs, classID, "testing");
        });
    }

    @Test()
    void oneIDEditBatch() {
        String[] emptyIDs = new String[1];
        emptyIDs[0] = "4479badb-de18-4316-a529-996bf567518e";
        var classID = "CT22_FR_Linux_0";
        var response = studentService.updateSelectedStudentStatus(emptyIDs, classID, "testing");
        var get = studentClassRepository.findByStudentIdAndClassEntityId(emptyIDs[0], classID).orElseThrow();
        Assertions.assertAll(
                () -> Assertions.assertTrue(response.containsKey("success")),
                () -> Assertions.assertEquals(get.getAttendingStatus(), "testing")
        );
    }


    @Test()
    void fourIDEditBatch() {
        String[] studentIds = new String[4];
        studentIds[0] = "fda00fff-75fc-4cff-bf62-97b8fdccecc6";
        studentIds[1] = "2d1d9437-e154-44e4-828a-5d478949e80a";
        studentIds[2] = "4479badb-de18-4316-a529-996bf567518e";
        studentIds[3] = "165367f4-ff65-4550-a65c-64169f6729c9";
        var classID = "CT23_IN_DevOps_0";
        var response = studentService.updateSelectedStudentStatus(studentIds, classID, "testing");

        Assertions.assertAll(
                () -> Assertions.assertTrue(response.containsKey("success")),
                () -> {
                    for (String studentId : studentIds) {
                        var get = studentClassRepository.findByStudentIdAndClassEntityId(studentId, classID).orElseThrow();
                        Assertions.assertEquals(get.getAttendingStatus(), "testing");
                    }
                }
        );
    }

    @Test
    void statusMismatchEditBatch() {
        String[] studentIds = new String[3];
        studentIds[0] = "02b56502-dda2-4a79-9a7e-8d0ac488a2ab";
        studentIds[1] = "7187f206-b6e9-4e54-8372-c667342845ab";
        studentIds[2] = "fda00fff-75fc-4cff-bf62-97b8fdccecc6";

        var classID = "CT23_IN_DevOps_0";

        assertThrows(StatusMismatchException.class, () -> studentService.updateSelectedStudentStatus(studentIds, classID, "testing"));
    }


    @Test
    void classMismatchEditBatch() {
        String[] studentIds = new String[2];
        studentIds[0] = "165367f4-ff65-4550-a65c-64169f6729c9";
        studentIds[1] = "c04e7911-d7c6-4045-b70d-c3f28181c602";

        var classID = "CT24_FR_Java_0";
        Assertions.assertThrows(NoSuchElementException.class,
                () -> studentService.updateSelectedStudentStatus(studentIds, classID, "testing"));

    }

    @Test
    void fetchRecommendEmpty() {
        Assertions.assertThrows(NoSuchElementException.class, () ->
                studentService.fetchRecommendClass("", ""));
    }

    @Test
    void notExistClassFetchRecommend() {
        Assertions.assertThrows(NoSuchElementException.class, () ->
                studentService.fetchRecommendClass("random_class", ""));
    }


    @Test
    void notExistStudentFetchRecommend() {
        Assertions.assertFalse(
                studentService.fetchRecommendClass("HCM24_FR_Java_0", "").isEmpty());
    }

    @Test
    void fetchRecommendClassSuccess_first() {
        String[] expectedClass = new String[1];
        expectedClass[0] = "HCM24_FR_Java_0";
        var outputIDs = studentService
                .fetchRecommendClass("CT24_IN_Java_0", "165367f4-ff65-4550-a65c-64169f6729c9")
                .stream()
                .map(ClassEntityDTO::getId)
                .toArray();
        Assertions.assertArrayEquals(outputIDs, expectedClass);

    }


    @Test
    void fetchRecommendClassSuccess_second() {
        String[] expectedClass = new String[2];
//        expectedClass[0] = "HCM24_FR_Linux_0";
        expectedClass[0] = "DN21_FR_Linux_0";
        expectedClass[1] = "HL21_IN_Linux_0";

        var outputIDs = studentService
                .fetchRecommendClass("CT22_FR_Linux_0", "c04e7911-d7c6-4045-b70d-c3f28181c602")
                .stream()
                .map(ClassEntityDTO::getId)
                .toArray();
        Assertions.assertArrayEquals(outputIDs, expectedClass);

    }


    @Test
    void fetchRecommendClassSuccess_empty() {
        var outputIDs = studentService
                .fetchRecommendClass("HCM24_IN_Linux_0", "c04e7911-d7c6-4045-b70d-c3f28181c602");
        Assertions.assertTrue(outputIDs.isEmpty());

    }


    @Test
    void fetchRecommendClassSuccess_single_empty() {
        var outputIDs = studentService
                .fetchRecommendClass("HCM22_IN_NET_0", "c04e7911-d7c6-4045-b70d-c3f28181c602");
        Assertions.assertTrue(outputIDs.isEmpty());

    }
}
