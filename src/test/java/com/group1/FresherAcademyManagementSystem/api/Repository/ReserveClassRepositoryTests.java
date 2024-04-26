package com.group1.FresherAcademyManagementSystem.api.Repository;

import com.group1.FresherAcademyManagementSystem.models.ClassEntity;
import com.group1.FresherAcademyManagementSystem.models.Reserved_Class;
import com.group1.FresherAcademyManagementSystem.models.Student;
import com.group1.FresherAcademyManagementSystem.repositories.ClassRepository;
import com.group1.FresherAcademyManagementSystem.repositories.ReserveClassRepository;
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

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ReserveClassRepositoryTests {

    @Autowired
    ReserveClassRepository reserveClassRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    ClassRepository classRepository;

    @Test
    public void ReserveClassRepository_FindAllByStudentId_ReturnReserveClassList(){
        Reserved_Class reservedClass = new Reserved_Class();
        Student student = new Student();
        student.setId("ST01");
        studentRepository.save(student);
        reservedClass.setStudent(student);

        reserveClassRepository.save(reservedClass);

        List<Reserved_Class> result = reserveClassRepository.findAllByStudent_Id("ST01");

        Assertions.assertThat(result).isNotEmpty();
    }

    @Test
    public void ReserveClassRepository_existsByClassEntityIdAndStudentId_ReturnTrue(){
        Reserved_Class reservedClass = new Reserved_Class();
        Student student = new Student();
        student.setId("ST01");
        ClassEntity classEntity = new ClassEntity();
        classEntity.setId("SC01");
        classRepository.save(classEntity);
        studentRepository.save(student);
        reservedClass.setStudent(student);
        reservedClass.setClassEntity(classEntity);

        reserveClassRepository.save(reservedClass);

        Boolean result = reserveClassRepository.existsByClassEntity_IdAndStudent_Id("SC01", "ST01");

        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void ReserveClassRepository_CountAllByStudentId_ReturnMoreThanZero(){
        Reserved_Class reservedClass = new Reserved_Class();
        Student student = new Student();
        student.setId("ST01");
        ClassEntity classEntity = new ClassEntity();
        classEntity.setId("SC01");
        classRepository.save(classEntity);
        studentRepository.save(student);
        reservedClass.setStudent(student);
        reservedClass.setClassEntity(classEntity);

        reserveClassRepository.save(reservedClass);

        int result = reserveClassRepository.countAllByStudent_Id("ST01");

        Assertions.assertThat(result).isGreaterThan(0);
    }

    @Test
    public void ReserveClassRepository_findByIdOrClassNameOrStatus_ReturnReserveClassPage(){
        Reserved_Class reservedClass = new Reserved_Class();
        Student student = new Student();
        student.setId("ST01");
        ClassEntity classEntity = new ClassEntity();
        classEntity.setId("SC01");
        classRepository.save(classEntity);
        studentRepository.save(student);
        reservedClass.setStudent(student);
        reservedClass.setClassEntity(classEntity);

        reserveClassRepository.save(reservedClass);

        Pageable pageable = PageRequest.of(1,5);

        Page<Reserved_Class> result = reserveClassRepository.findByIdOrClassNameOrStatus("t",pageable,true,true,true,true);

        Assertions.assertThat(result.getSize()).isGreaterThan(0);
    }

    @Test
    public void ReserveClassRepository_findAll_ReturnReserveClassPage(){
        Reserved_Class reservedClass = new Reserved_Class();
        Student student = new Student();
        student.setId("ST01");
        ClassEntity classEntity = new ClassEntity();
        classEntity.setId("SC01");
        classRepository.save(classEntity);
        studentRepository.save(student);
        reservedClass.setStudent(student);
        reservedClass.setClassEntity(classEntity);

        reserveClassRepository.save(reservedClass);

        Pageable pageable = PageRequest.of(1,5);

        Page<Reserved_Class> result = reserveClassRepository.findAll(pageable);

        Assertions.assertThat(result.getSize()).isGreaterThan(1);
    }

    @Test
    public void ReserveClassRepository_findByStudentIdAndClassId_ReturnReserveClass(){
        Reserved_Class reservedClass = new Reserved_Class();
        Student student = new Student();
        student.setId("ST01");
        ClassEntity classEntity = new ClassEntity();
        classEntity.setId("SC01");
        classRepository.save(classEntity);
        studentRepository.save(student);
        reservedClass.setStudent(student);
        reservedClass.setClassEntity(classEntity);

        reserveClassRepository.save(reservedClass);

        Reserved_Class result = reserveClassRepository.findByStudentIdAndClassId("ST01","SC01");

        Assertions.assertThat(result).isNotNull();
    }

    @Test
    public void ReserveClassRepository_findAllReserveClass_ReturnReserveClassList(){
        Reserved_Class reservedClass = new Reserved_Class();
        Student student = new Student();
        student.setId("ST01");
        ClassEntity classEntity = new ClassEntity();
        classEntity.setId("SC01");
        classRepository.save(classEntity);
        studentRepository.save(student);
        reservedClass.setStudent(student);
        reservedClass.setClassEntity(classEntity);

        reserveClassRepository.save(reservedClass);

        List<Reserved_Class> result = reserveClassRepository.findAllReservedClasses();

        Assertions.assertThat(result).isNotEmpty();
    }
}
