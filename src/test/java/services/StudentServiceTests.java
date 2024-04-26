package services;


import com.group1.FresherAcademyManagementSystem.models.*;
import com.group1.FresherAcademyManagementSystem.repositories.ScoreRepository;
import com.group1.FresherAcademyManagementSystem.repositories.StudentClassRepository;
import com.group1.FresherAcademyManagementSystem.repositories.StudentRepository;
import com.group1.FresherAcademyManagementSystem.services.ExcelUploadService;
import com.group1.FresherAcademyManagementSystem.services.Impl.ScoreServiceImpl;
import com.group1.FresherAcademyManagementSystem.services.Impl.StudentServiceImpl;
import com.group1.FresherAcademyManagementSystem.services.StudentService;
import jakarta.persistence.EntityNotFoundException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.*;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@SpringBootConfiguration
public class StudentServiceTests {

    @Mock
    private StudentRepository studentRepository;
    @Mock
    private StudentClassRepository studentClassRepository;
    @Mock
    private ExcelUploadService excelUploadService;
    @Mock
    private Workbook workbook;

    @Mock
    private Sheet sheet;
    @InjectMocks
    private StudentServiceImpl studentServiceImpl;

    @InjectMocks
    private ScoreServiceImpl scoreServiceImpl;



    @Test
    public void testDeletedStudentById() {
        //Arrange
        Student existingStudent = Student.builder()
                .id("1")
                .status("inactive")
                .build();
        studentRepository.save(existingStudent);
        // Mock behavior of student repository
        when(studentRepository.findById(existingStudent.getId())).thenReturn(Optional.ofNullable(existingStudent));
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        //Act
        studentServiceImpl.deleteStudentById(existingStudent.getId());

        //Assert
        Assertions.assertThat(existingStudent.getStatus()).isEqualTo("disable");
    }

    @Test
    public void testDeletedStudentWithActive() {
        //Arrange
        Student existingStudent = Student.builder()
                .id("2")
                .status("active")
                .build();
        studentRepository.save(existingStudent);

        // Mock behavior of student repository
        when(studentRepository.findById(existingStudent.getId())).thenReturn(Optional.ofNullable(existingStudent));
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        //Act and Assert
        assertThrows(UnsupportedOperationException.class, () -> {
            studentServiceImpl.deleteStudentById(existingStudent.getId());
        });
    }

    @Test
    public void testDeletedStudentWithScores() {
        Score score1 = new Score();
        score1.setScore(5f);
        Score score2 = new Score();
        score2.setScore(8.5f);
        //Arrange
        Student existingStudent = Student.builder()
                .id("3")
                .status("active")
                .scores(Arrays.asList(score1,score2))
                .build();
        studentRepository.save(existingStudent);

        // Mock behavior of student repository
        when(studentRepository.findById(existingStudent.getId())).thenReturn(Optional.of(existingStudent));

        //Act and Assert
        assertThrows(UnsupportedOperationException.class, () -> {
            studentServiceImpl.deleteStudentById(existingStudent.getId());
        });
    }

    @Test
    public void testDeletedStudentWithNull() {
        //Arrange
        Student existingStudent = Student.builder()
                .id(null)
                .status("active")
                .build();
        studentRepository.save(existingStudent);

        when(studentRepository.findById(null)).thenReturn(Optional.empty());

        //Act and Assert
        assertThrows(EntityNotFoundException.class, () ->{
            studentServiceImpl.deleteStudentById(null);
        });
    }

    @Test
    public void testDeletedStudentByIdInClassWhenStudentNotFound() {
        // Arrange
        String studentId = "ST001";
        String classId = "SC1";

        Student student = new Student();
        student.setId(studentId);
        ClassEntity classEntity = new ClassEntity();
        classEntity.setId(classId);

        Student_Class existingStudent = Student_Class.builder()
                .student(student)
                .classEntity(classEntity)
                .build();

        when(studentClassRepository.findByStudentIdAndClassId(existingStudent.getStudent().getId(),existingStudent.getClassId())).thenReturn(new ArrayList<>());
        assertThrows(EntityNotFoundException.class, () -> {
           studentServiceImpl.deleteStudentByIdInClass(studentId,classId);
        });
    }

//    @Test
//    public void testDeleteStudentByIdInClassWhenClassStatusIsPlanning() {
//        // Arrange
//        String studentId = "ST001";
//        String classId = "SC1";
//
//        ClassEntity classEntity = new ClassEntity();
//        classEntity.setStatus("in progress");
//
//        Student student = new Student();
//        student.setId(studentId);
//        classEntity.setId(classId);
//
//        Student_Class existingStudent = Student_Class.builder()
//                .student(student)
//                .classEntity(classEntity)
//                .build();
//
//        when(studentClassRepository.findByStudentIdAndClassId(eq(studentId), eq(classId)))
//                .thenReturn(Collections.singletonList(existingStudent)); // Return a list containing existingStudent
//
//        // Mock the behavior of saveAll
//        when(studentClassRepository.saveAll(anyList()))
//                .thenReturn(Collections.singletonList(existingStudent)); // Return a list containing existingStudent
//
//        //Act
//        studentServiceImpl.deleteStudentByIdInClass(studentId,classId);
//        //Assert
//        Assertions.assertThat(existingStudent.getAttendingStatus()).isEqualTo("deleted");
//
//    }

    @Test
    public void testUpdateCertificate() {
        String studentId = "ST001";
        String classid = "SC1";

        Student student = new Student();
        student.setId(studentId);
        ClassEntity classEntity = new ClassEntity();
        classEntity.setId(classid);

        //Arrange
        Student_Class student1 = Student_Class
                .builder()
                .student(student)
                .classEntity(classEntity)
                .certificationDate(LocalDate.parse("2019-10-10"))
                .certificationStatus("Good")
                .build();

        studentClassRepository.save(student1);

        List<Student_Class> sc = studentClassRepository.findByStudentIdAndClassId(student1.getStudent().getId(),student1.getClassId());

        for (Student_Class st:sc) {
            st.setCertificationDate(LocalDate.parse("2023-10-10"));
            st.setCertificationStatus("Bad");
        }

        //Act
        List<Student_Class> update = studentClassRepository.saveAll(sc);

        //Assertions
        for (Student_Class st1: update) {
            Assertions.assertThat(st1.getCertificationDate()).isNotNull();
            Assertions.assertThat(st1.getCertificationStatus()).isEqualTo("Bad");
        }

    }

//    @Test
//    public void testWriteScoresToExcel_Success() throws IOException {
//        // Initialize Mockito annotations
//        MockitoAnnotations.openMocks(this);
//
//        // Mock data
//        List<Score> scores = new ArrayList<>();
//        // Add some scores to the list
//
//        String filePath = "Score.xlsx";
//
//        // Mock workbook creation
//        when(workbook.createSheet(any())).thenReturn(sheet);
//
//        // Mock workbook writing
//        when(sheet.createRow(anyInt())).thenReturn(mock(Row.class));
//        when(sheet.createRow(0)).thenReturn(mock(Row.class));
//        when(sheet.createRow(1)).thenReturn(mock(Row.class));
//
//        String classId = "SC1";
//        // Call method
//        scoreServiceImpl.writeScoresToExcel(scores, filePath,classId);
//
//        //Assert
//        File file = new File(filePath);
//        assertTrue(file.exists());
//    }
    
    
}
