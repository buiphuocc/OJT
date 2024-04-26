package com.group1.FresherAcademyManagementSystem.api.Repository;

import com.group1.FresherAcademyManagementSystem.models.ClassEntity;
import com.group1.FresherAcademyManagementSystem.models.Student;
import com.group1.FresherAcademyManagementSystem.models.Student_Class;
import com.group1.FresherAcademyManagementSystem.repositories.ClassRepository;
import com.group1.FresherAcademyManagementSystem.repositories.StudentClassRepository;
import com.group1.FresherAcademyManagementSystem.repositories.StudentRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class StudentRepositoryTests {

     @Autowired
     private StudentRepository studentRepository;

     @Autowired
     private ClassRepository classRepository;

     @Autowired
     private StudentClassRepository studentClassRepository;

    @Test
    public void StudentRepository_FindTopByOrderByIdDesc_ReturnBiggestId(){
        Student student1 = new Student();
        Student student2 = new Student();
        student1.setId("ST01");
        student2.setId("ST02");

        studentRepository.save(student1);
        studentRepository.save(student2);

        Optional<Student> biggestId = studentRepository.findTopByOrderByIdDesc();

        Assertions.assertThat(biggestId).isNotEmpty();
        Assertions.assertThat(biggestId.get().getId()).isEqualTo("ST02");
    }

    @Test
    public void StudentRepository_FindByStudentClassesClassEntityClassId_ReturnListOfStudent(){
        Student student1 = new Student();
        ClassEntity classEntity = new ClassEntity();
        Student_Class studentClass = new Student_Class();

        student1.setId("ST01");
        classEntity.setId("SC01");
        studentClass.setStudent(student1);
        studentClass.setClassEntity(classEntity);

        studentRepository.save(student1);
        classRepository.save(classEntity);
        studentClassRepository.save(studentClass);

        List<Student> result = studentRepository.findByStudentClasses_ClassEntityClassId("SC01");

        Assertions.assertThat(result).isNotEmpty();
    }

    @Test
    public void StudentRepository_FindByIdOrFullNameOrEmailOrPhoneOrStatus_ReturnStudent(){
        Student student1 = new Student();
        student1.setId("ST01");
        student1.setFullName("test");
        student1.setEmail("test");
        student1.setPhone("test");
        student1.setStatus("active");

        Pageable pageable = PageRequest.of(1,1);

        studentRepository.save(student1);

        Page<Student> result = studentRepository.findByIdOrFullNameOrEmailOrPhoneOrStatus("t", pageable, true, true, true, true, true);

        Assertions.assertThat(result.getSize()).isGreaterThan(0);
    }

    @Test
    public void StudentRepository_FindAll_ReturnStudent(){
        Student student1 = new Student();
        student1.setId("ST01");
        student1.setFullName("test");
        student1.setEmail("test");
        student1.setPhone("test");
        student1.setStatus("active");

        Pageable pageable = PageRequest.of(1,1);

        studentRepository.save(student1);

        Page<Student> result = studentRepository.findAll(pageable);

        Assertions.assertThat(result.getSize()).isGreaterThan(0);
    }

    @Test
    public void StudentRepository_FindByStatus_ReturnStudent(){
        Student student1 = new Student();
        student1.setId("ST01");
        student1.setFullName("test");
        student1.setEmail("test");
        student1.setPhone("test");
        student1.setStatus("active");

        Pageable pageable = PageRequest.of(1,5);

        studentRepository.save(student1);

        Page<Student> result = studentRepository.findByStatus(pageable, true,true);

        Assertions.assertThat(result.getSize()).isGreaterThan(0);
    }

    @Test
    public void StudentRepository_FindByIdIn_ReturnStudent(){
        Student student1 = new Student();
        Student student2 = new Student();
        student1.setId("ST01");
        student2.setId("ST02");

        studentRepository.save(student1);
        studentRepository.save(student2);

        List<String> ids = new ArrayList<>();
        ids.add("ST01");
        ids.add("ST02");

        List<Student> result =studentRepository.findByIdIn(ids);

        Assertions.assertThat(result).isNotEmpty();
    }

    @Test
    public void StudentRepositoy_FindByIdOrFullNameOrEmailOrPhoneOrStatusForExport_ReturnStudent(){
        Student student1 = new Student();
        student1.setId("ST01");
        student1.setFullName("test");
        student1.setEmail("test");
        student1.setPhone("test");
        student1.setStatus("active");

        studentRepository.save(student1);

        List<Student> result = studentRepository.findByIdOrFullNameOrEmailOrPhoneOrStatusForExport("t", true, true, true, true, true);

        Assertions.assertThat(result).isNotEmpty();
    }

    @Test
    public void StudentRepository_FindAllForExport_ReturnStudent(){
        Student student1 = new Student();
        student1.setId("ST01");
        student1.setFullName("test");
        student1.setEmail("test");
        student1.setPhone("test");
        student1.setStatus("active");

        studentRepository.save(student1);

        List<Student> result = studentRepository.findAllForExport();

        Assertions.assertThat(result).isNotEmpty();
    }

    @Test
    public void StudentRepository_FindByStatusForExport_ReturnStudent(){
        Student student1 = new Student();
        student1.setId("ST01");
        student1.setFullName("test");
        student1.setEmail("test");
        student1.setPhone("test");
        student1.setStatus("active");

        studentRepository.save(student1);

        List<Student> result = studentRepository.findByStatusForExport(true,true);

        Assertions.assertThat(result).isNotEmpty();
    }

    @Test
    public void StudentRepository_FindByIdOrFullNameOrEmailOrPhoneOrStatusAndClassId_ReturnStudent(){
        Student student1 = new Student();
        ClassEntity classEntity = new ClassEntity();
        Student_Class studentClass = new Student_Class();

        student1.setId("ST01");
        classEntity.setId("SC01");
        studentClass.setStudent(student1);
        studentClass.setClassEntity(classEntity);
        studentClass.setAttendingStatus("finished");

        studentRepository.save(student1);
        classRepository.save(classEntity);
        studentClassRepository.save(studentClass);

        List<Student> result = studentRepository.findByIdOrFullNameOrEmailOrPhoneOrStatusAndClassId("t",true,true,true,true,true,"SC01");

        Assertions.assertThat(result).isNotEmpty();
    }

    @Test
    public void StudentRepository_FindByStatusAndClassId_ReturnStudent(){
        Student student1 = new Student();
        ClassEntity classEntity = new ClassEntity();
        Student_Class studentClass = new Student_Class();

        student1.setId("ST01");
        classEntity.setId("SC01");
        studentClass.setStudent(student1);
        studentClass.setClassEntity(classEntity);
        studentClass.setAttendingStatus("finished");

        studentRepository.save(student1);
        classRepository.save(classEntity);
        studentClassRepository.save(studentClass);

        List<Student> result = studentRepository.findByStatusAndClassId(false,true,"SC01");

        Assertions.assertThat(result).isNotEmpty();
    }

    @Test
    void testFindByIdOrFullNameOrEmailOrPhoneOrStatus() {
        Student student1 = Student.builder()
                .id("1")
                .fullName("John Doe")
                .phone("1234567890")
                .email("john@example.com")
                .status("active")
                .build();
        Student student2 = Student.builder()
                .id("2")
                .fullName("Jane Doe")
                .phone("9876543210")
                .email("jane@example.com")
                .status("inactive")
                .build();
        List<Student> students = Arrays.asList(student1, student2);
        studentRepository.saveAll(students);

        Page<Student> result = studentRepository.findByIdOrFullNameOrEmailOrPhoneOrStatus(
                "John", PageRequest.of(0, 10), true, false, false, false, false);

        assertEquals(1, result.getContent().size());
    }
    
    @Test
    void testFindByIdOrFullNameOrEmailOrPhoneOrStatus_FullName() {
        Student student1 = Student.builder()
                .id("1")
                .fullName("John Doe")
                .phone("1234567890")
                .email("john@example.com")
                .status("active")
                .build();
        Student student2 = Student.builder()
                .id("2")
                .fullName("Jane Doe")
                .phone("9876543210")
                .email("jane@example.com")
                .status("inactive")
                .build();
        List<Student> students = Arrays.asList(student1, student2);
        studentRepository.saveAll(students);

        Page<Student> result = studentRepository.findByIdOrFullNameOrEmailOrPhoneOrStatus(
                "John", PageRequest.of(0, 10), true, false, false, false, false);

        assertEquals(1, result.getContent().size());
    }

    @Test
    void testFindByIdOrFullNameOrEmailOrPhoneOrStatus_Email() {
        Student student1 = Student.builder()
                .id("1")
                .fullName("John Doe")
                .phone("1234567890")
                .email("john@example.com")
                .status("active")
                .build();
        Student student2 = Student.builder()
                .id("2")
                .fullName("Jane Doe")
                .phone("9876543210")
                .email("jane@example.com")
                .status("inactive")
                .build();
        List<Student> students = Arrays.asList(student1, student2);
        studentRepository.saveAll(students);

        Page<Student> result = studentRepository.findByIdOrFullNameOrEmailOrPhoneOrStatus(
                "jane@example.com", PageRequest.of(0, 10), false, true, false, false, false);

        assertEquals(0, result.getContent().size());
    }

    @Test
    void testFindByIdOrFullNameOrEmailOrPhoneOrStatus_Phone() {
        Student student1 = Student.builder()
                .id("1")
                .fullName("John Doe")
                .phone("1234567890")
                .email("john@example.com")
                .status("active")
                .build();
        Student student2 = Student.builder()
                .id("2")
                .fullName("Jane Doe")
                .phone("9876543210")
                .email("jane@example.com")
                .status("inactive")
                .build();
        List<Student> students = Arrays.asList(student1, student2);
        studentRepository.saveAll(students);

        Page<Student> result = studentRepository.findByIdOrFullNameOrEmailOrPhoneOrStatus(
                "9876543210", PageRequest.of(0, 10), false, false, true, true, false);

        assertEquals(1, result.getContent().size());
    }

    @Test
    void testFindByIdOrFullNameOrEmailOrPhoneOrStatus_Status() {
        Student student1 = Student.builder()
                .id("1")
                .fullName("John Doe")
                .phone("1234567890")
                .email("john@example.com")
                .status("active")
                .build();
        Student student2 = Student.builder()
                .id("2")
                .fullName("Jane Doe")
                .phone("9876543210")
                .email("jane@example.com")
                .status("inactive")
                .build();
        List<Student> students = Arrays.asList(student1, student2);
        studentRepository.saveAll(students);

        Page<Student> result = studentRepository.findByIdOrFullNameOrEmailOrPhoneOrStatus(
                "", PageRequest.of(0, 10), false, false, false, false, false);

        assertEquals(1, result.getContent().size());
    }

    @Test
    void testFindByIdOrFullNameOrEmailOrPhoneOrStatus_MultipleCriteria() {
        Student student1 = Student.builder()
                .id("1")
                .fullName("John Doe")
                .phone("1234567890")
                .email("john@example.com")
                .status("active")
                .build();
        Student student2 = Student.builder()
                .id("2")
                .fullName("Jane Doe")
                .phone("9876543210")
                .email("jane@example.com")
                .status("inactive")
                .build();
        List<Student> students = Arrays.asList(student1, student2);
        studentRepository.saveAll(students);

        Page<Student> result = studentRepository.findByIdOrFullNameOrEmailOrPhoneOrStatus(
                "john", PageRequest.of(0, 10), true, false, false, false, false);

        assertEquals(1, result.getContent().size());
    }

}
