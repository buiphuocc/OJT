package com.group1.FresherAcademyManagementSystem.services;

import com.group1.FresherAcademyManagementSystem.dtos.student_edit_dto.ReservedClassDTO;
import com.group1.FresherAcademyManagementSystem.dtos.student_edit_dto.StudentGeneralInfoDTO;
import com.group1.FresherAcademyManagementSystem.dtos.student_edit_dto.StudentOtherInfoDTO;
import com.group1.FresherAcademyManagementSystem.services.Impl.StudentEditServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.NoSuchElementException;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Execution(ExecutionMode.CONCURRENT)
@Transactional
@Sql({
        "/db/student.sql",
        "/db/training_program.sql",
        "/db/class_entity.sql",
        "/db/reserved_class.sql",
        "/db/student_class.sql"
})
public class StudentEditServiceTest {
    @Autowired
    StudentEditServiceImpl studentEditService;

    @Test
    void fetchNonExistProfile() {
        Assertions.assertThrows(
                NoSuchElementException.class,
                () -> studentEditService.fetchStudentInfo("TEST_ID"));
    }

    @Test
    void fetchEmptyProfile() {
        Assertions.assertThrows(
                NoSuchElementException.class,
                () -> studentEditService.fetchStudentInfo(""));
    }

    @Test
    void fetchEmptyReservedClass() {
        var data = studentEditService
                .fetchStudentInfo("2d1d9437-e154-44e4-828a-5d478949e80a");
        Assertions.assertAll(
                () -> Assertions.assertTrue(data.getReservedClasses().isEmpty()),
                () -> Assertions.assertEquals(data.getStudentClasses().size(), 2)
        );
    }

    @Test
    void fetchEmptyClass() {
        var data = studentEditService
                .fetchStudentInfo("bc4c0c3d-a3cc-4002-98a9-331021a3436c");
        Assertions.assertAll(
                () -> Assertions.assertTrue(data.getReservedClasses().isEmpty()),
                () -> Assertions.assertTrue(data.getStudentClasses().isEmpty())
        );
    }

    @Test
    void hasReservedClass() {
        var data = studentEditService.fetchStudentInfo("c5542f8a-e829-4205-8ac4-eb61ea7da4af");

        var classId = "HCM22_FR_Java_0";

        Assertions.assertAll(
                () -> Assertions.assertEquals(data.getReservedClasses().get(0).getClassID(), classId),
                () -> Assertions.assertEquals(data.getStudentClasses().get(0).getId(), classId)
        );
    }

    @Test
    void fetchComplete() {
        var data = studentEditService.fetchStudentInfo("da3f121a-68ba-4c08-9012-2b24b80b538a");

        var gData = data.getGeneral();
        var oData = data.getOthers();
        var reCls = data.getReservedClasses();
        var stCls = data.getStudentClasses();

        Assertions.assertAll(
                () -> Assertions.assertEquals(gData.getId(), "da3f121a-68ba-4c08-9012-2b24b80b538a"),
                () -> Assertions.assertEquals(gData.getFullName(), "Tran Hai Tang"),
                () -> Assertions.assertEquals(gData.getGender(), "Male"),
                () -> Assertions.assertEquals(gData.getDob().toString(), "2000-02-28 00:00:00.0"),
                () -> Assertions.assertEquals(gData.getStatus(), "Inactive"),
                () -> Assertions.assertEquals(gData.getPhone(), "479.995.5342"),
                () -> Assertions.assertEquals(gData.getEmail(), "TangHT@random.email"),
                () -> Assertions.assertEquals(gData.getAddress(), "Nam Ban"),
                () -> Assertions.assertEquals(gData.getArea(), "Da Lat"),
                () -> Assertions.assertEquals(oData.getSchool(), "HUS"),
                () -> Assertions.assertEquals(oData.getMajor(), "Cloud Computing"),
                () -> Assertions.assertEquals(oData.getRecer(), "Nguyen Hai Minh"),
                () -> Assertions.assertEquals(oData.getGpa(), 3.44f),
                () -> Assertions.assertEquals(oData.getYearOfGraduation().toString(), "2023-08-11"),
                () -> Assertions.assertEquals(reCls.size(), 1),
                () -> Assertions.assertEquals(stCls.size(), 3)
        );
    }

    @Test
    void fetchNonExistIdReservableClass() {
        Assertions.assertThrows(NoSuchElementException.class,
                () -> studentEditService.fetchReservableClass("TEST_ID")
        );
    }

    @Test
    void fetchEmptyIdReservableClass() {
        Assertions.assertThrows(NoSuchElementException.class,
                () -> studentEditService.fetchReservableClass("")
        );
    }

    @Test
    void fetchEmptyReservable() {
        Assertions.assertEquals(studentEditService.fetchReservableClass("120f6fc6-d254-432e-8fdc-34ef4a0b165b").size(), 0);
    }

    @Test
    void fetchReservable() {
        Assertions.assertEquals(studentEditService.fetchReservableClass("b55bbf56-322f-4a2e-a7c6-90cb6447f45a").size(), 2);
    }

    @Test
    void fetchNonExistIdEnrollableClass() {
        Assertions.assertThrows(NoSuchElementException.class,
                () -> studentEditService.fetchEnrollableClass("TEST_ID")
        );
    }

    @Test
    void fetchEmptyIdEnrollableClass() {
        Assertions.assertThrows(NoSuchElementException.class,
                () -> studentEditService.fetchEnrollableClass("")
        );
    }


    @Test
    void fetchEnrollable() {
        Assertions.assertEquals(studentEditService.fetchEnrollableClass("b55bbf56-322f-4a2e-a7c6-90cb6447f45a").size(), 8);
    }

    @Test
    void addReservedClassForStudent() {
        var studentID = "b55bbf56-322f-4a2e-a7c6-90cb6447f45a";
        var reserveClassSt = ReservedClassDTO.builder()
                .reasonReverse("Test")
                .classID("DN23_IN_Java_0")
                .build();

        Assertions.assertEquals(studentEditService.addReservedClass(studentID, reserveClassSt).size(), 2);
    }

    @Test
    void addReservedClassForStudentDuplicate() {
        var studentID = "b55bbf56-322f-4a2e-a7c6-90cb6447f45a";
        var reserveClassSt = ReservedClassDTO.builder()
                .reasonReverse("Test")
                .classID("DN23_IN_Java_0")
                .build();

        Assertions.assertAll(
                () -> Assertions.assertEquals(studentEditService.addReservedClass(studentID, reserveClassSt).size(), 2),
                () -> Assertions.assertThrows(IllegalArgumentException.class,
                        () -> studentEditService.addReservedClass(studentID, reserveClassSt).size())
        );
    }

    @Test
    void addReservedClassForStudent_emptyStudentID() {
        var studentID = "";
        var reserveClassSt = ReservedClassDTO.builder()
                .reasonReverse("Test")
                .classID("DN23_IN_Java_0")
                .build();

        Assertions.assertAll(
                () -> Assertions.assertThrows(NoSuchElementException.class,
                        () -> studentEditService.addReservedClass(studentID, reserveClassSt))
        );
    }


    @Test
    void addReservedClassForStudent_emptyClassID() {
        var studentID = "b55bbf56-322f-4a2e-a7c6-90cb6447f45a";
        var reserveClassSt = ReservedClassDTO.builder()
                .reasonReverse("Test")
                .classID("")
                .build();

        Assertions.assertAll(
                () -> Assertions.assertThrows(NoSuchElementException.class,
                        () -> studentEditService.addReservedClass(studentID, reserveClassSt))
        );
    }


    @Test
    void addReservedClassForStudent_emptyClassID_emptyStudentID() {
        var studentID = "";
        var reserveClassSt = ReservedClassDTO.builder()
                .reasonReverse("Test")
                .classID("")
                .build();

        Assertions.assertAll(
                () -> Assertions.assertThrows(NoSuchElementException.class,
                        () -> studentEditService.addReservedClass(studentID, reserveClassSt))
        );
    }


    @Test
    void addClassForStudent() {
        var studentID = "1c8425fd-ef42-46b8-a2a4-273a5d144d97";
        var classSt = "DN23_IN_Java_0";
        Assertions.assertEquals(studentEditService.addClassEntity(studentID, classSt).size(), 3);
    }

    @Test
    void addClassForStudentDuplicate() {
        var studentID = "1c8425fd-ef42-46b8-a2a4-273a5d144d97";
        var classSt = "DN23_IN_Java_0";
        Assertions.assertAll(
                () -> Assertions.assertEquals(studentEditService.addClassEntity(studentID, classSt).size(), 3),
                () -> Assertions.assertThrows(IllegalArgumentException.class,
                        () -> studentEditService.addClassEntity(studentID, classSt))
        );
    }

    @Test
    void addClassForStudent_emptyStudentID() {
        var studentID = "";
        var classSt = "DN23_IN_Java_0";

        Assertions.assertThrows(NoSuchElementException.class,
                () -> studentEditService.addClassEntity(studentID, classSt));
    }


    @Test
    void addClassForStudent_emptyClassID() {
        var studentID = "1c8425fd-ef42-46b8-a2a4-273a5d144d97";
        var classSt = "";

        Assertions.assertAll(
                () -> Assertions.assertThrows(NoSuchElementException.class,
                        () -> studentEditService.addClassEntity(studentID, classSt))
        );
    }


    @Test
    void addClassForStudent_emptyClassID_emptyStudentID() {
        var studentID = "";
        var classId = "";

        Assertions.assertThrows(NoSuchElementException.class,
                () -> studentEditService.addClassEntity(studentID, classId));
    }

    @Test
    void updateGeneralInfo() {
        var studentId = "b55bbf56-322f-4a2e-a7c6-90cb6447f45a";
        var gInfo = StudentGeneralInfoDTO.builder()
                .id(studentId)
                .fullName("Generic Name")
                .gender("Test gender")
                .dob(Date.valueOf(LocalDate.of(2022, 9, 6)))
                .status("Test status")
                .phone("0123456789")
                .email("Test@test.email")
                .address("Test address")
                .area("Test area")
                .build();

        Assertions.assertEquals(studentEditService.updateStudentGeneralInfo(studentId, gInfo), gInfo);
    }


    @Test
    void updateGeneralInfo_missingField() {
        var studentId = "b55bbf56-322f-4a2e-a7c6-90cb6447f45a";
        var gInfo = StudentGeneralInfoDTO.builder()
                .id(studentId)
                .build();

        Assertions.assertEquals(studentEditService.updateStudentGeneralInfo(studentId, gInfo), gInfo);
    }


    @Test
    void updateGeneralInfo_missingID() {
        var studentId = "";
        var gInfo = StudentGeneralInfoDTO.builder()
                .id(studentId)
                .fullName("Generic Name")
                .gender("Test gender")
                .dob(Date.valueOf(LocalDate.of(2022, 9, 6)))
                .status("Test status")
                .phone("0123456789")
                .email("Test@test.email")
                .address("Test address")
                .area("Test area")
                .build();

        Assertions.assertThrows(NoSuchElementException.class,
                () -> studentEditService.updateStudentGeneralInfo(studentId, gInfo));
    }


    @Test
    void updateGeneralInfo_missingID_missingField() {
        var studentId = "";
        var gInfo = StudentGeneralInfoDTO.builder()
                .build();

        Assertions.assertThrows(NoSuchElementException.class,
                () -> studentEditService.updateStudentGeneralInfo(studentId, gInfo));
    }

    @Test
    void updateOtherInfo() {
        var studentId = "b55bbf56-322f-4a2e-a7c6-90cb6447f45a";
        var oInfo = StudentOtherInfoDTO.builder()
                .school("Test school")
                .major("Test major")
                .recer("Test recer")
                .gpa(6.9f)
                .yearOfGraduation(LocalDate.of(2022, 6, 9))
                .build();

        Assertions.assertEquals(
                studentEditService.updateStudentOtherInfo(studentId, oInfo), oInfo
        );
    }

    @Test
    void updateOtherInfo_missingSId() {
        var studentId = "";
        var oInfo = StudentOtherInfoDTO.builder()
                .school("Test school")
                .major("Test major")
                .recer("Test recer")
                .gpa(6.9f)
                .yearOfGraduation(LocalDate.of(2022, 6, 9))
                .build();

        Assertions.assertThrows(NoSuchElementException.class,
                () -> studentEditService.updateStudentOtherInfo(studentId, oInfo)
        );
    }


    @Test
    void updateOtherInfo_missingSId_missingField() {
        var studentId = "";
        var oInfo = StudentOtherInfoDTO.builder()
                .build();

        Assertions.assertThrows(NoSuchElementException.class,
                () -> studentEditService.updateStudentOtherInfo(studentId, oInfo)
        );
    }

    @Test
    void updateOtherInfo_missingField() {
        var studentId = "b55bbf56-322f-4a2e-a7c6-90cb6447f45a";
        var oInfo = StudentOtherInfoDTO.builder()
                .build();

        Assertions.assertEquals(
                studentEditService.updateStudentOtherInfo(studentId, oInfo), oInfo
        );

    }

}
