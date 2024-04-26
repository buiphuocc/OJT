package com.group1.FresherAcademyManagementSystem.services.Impl;

import com.group1.FresherAcademyManagementSystem.dtos.ClassEntityDTO;
import com.group1.FresherAcademyManagementSystem.dtos.student_edit_dto.*;
import com.group1.FresherAcademyManagementSystem.mappers.ClassEntityMapper;
import com.group1.FresherAcademyManagementSystem.mappers.student_edit_mapper.*;
import com.group1.FresherAcademyManagementSystem.models.Module;
import com.group1.FresherAcademyManagementSystem.models.*;
import com.group1.FresherAcademyManagementSystem.repositories.*;
import com.group1.FresherAcademyManagementSystem.services.StudentEditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class StudentEditServiceImpl implements StudentEditService {

    private final StudentRepository studentRepository;

    private final StudentClassRepository studentClassRepository;

    private final ReserveClassRepository reserveClassRepository;

    private final ClassRepository classRepository;

    private final AssignmentRepository assignmentRepository;

    private final ScoreRepository scoreRepository;

    private final TrainingProgramModuleRepository trainingProgramModuleRepository;

    @Override
    public StudentViewEditDTO fetchStudentInfo(String id) {
        return studentRepository
                .findById(id)
                .map(StudentViewEditMapper::entityToDTO)
                .orElseThrow(NoSuchElementException::new);
    }


    @Override
    public StudentGeneralInfoDTO updateStudentGeneralInfo(String id, StudentGeneralInfoDTO info) {
        var student = studentRepository.findById(id).orElseThrow();

        var updated_student = StudentGeneralInfoMapper.patchEntity(student, info);

        student = studentRepository.save(updated_student);

        return StudentGeneralInfoMapper.mapToDTO(student);
    }

    @Override
    public StudentOtherInfoDTO updateStudentOtherInfo(String id, StudentOtherInfoDTO info) {
        var student = studentRepository.findById(id).orElseThrow();

        var updated_student = StudentOtherInfoMapper.patchEntity(student, info);

        student = studentRepository.save(updated_student);

        return StudentOtherInfoMapper.mapToDTO(student);
    }

    @Override
    public List<ClassEntityDTO> addClassEntity(String id, String classID) {
        if (studentClassRepository.existsByClassEntity_IdAndStudent_Id(classID, id))
            throw new IllegalArgumentException("Class " + classID + " is enrolled");

        var classData = classRepository.findById(classID).orElseThrow(
                () -> new NoSuchElementException("Class " + classID + " does not exist")
        );

        var student = studentRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Student " + id + "does not exist"));
        student.setStatus("active");

        if (!classData.getStatus().equalsIgnoreCase("Planning"))
            throw new IllegalArgumentException("Class \"" + classData.getId() + "\" is not in planning status");

        var studentClass = Student_Class.builder()
                .attendingStatus("inclass")
                .student(student)
                .gpaLevel(0.0f)
                .finalScore(0.0f)
                .classEntity(classData)
                .build();

        studentClassRepository.save(studentClass);

        var training_program_id = classData
                .getTrainingProgram()
                .getId();
        var moduleIdList = trainingProgramModuleRepository.findAllByProgramId(training_program_id)
                .stream()
                .map(TrainingProgram_Module::getModule)
                .map(Module::getId)
                .toList();

        var assignmentList = assignmentRepository
                .findByModuleIdIn(moduleIdList)
                .stream()
                .map(Assignment::getId)
                .toList();


        assignmentList.forEach(aLong -> {
            var ass = scoreRepository.findTopByAssignmentId(aLong).orElseThrow();
            ass.setStudent(student);
            scoreRepository.save(ass);
        });


        return studentClassRepository.findAllByStudent_Id(id)
                .stream()
                .map(Student_Class::getClassEntity)
                .map(ClassEntityMapper::mapToDTO)
                .toList();
    }

    @Deprecated
    @Override
    public List<ClassEntityDTO> fetchEnrolledClass(String id) {
        return studentClassRepository.findAllByStudent_Id(id)
                .stream()
                .map(Student_Class::getClassEntity)
                .map(ClassEntityMapper::mapToDTO)
                .toList();
    }

    @Override
    public List<ClassEntityDTO> fetchEnrollableClass(String id) {
        if (!studentRepository.existsById(id)) {
            throw new NoSuchElementException("No student with id \"" + id + "\" found");
        }
        var enrolledID = studentClassRepository.findAllByStudent_Id(id)
                .stream()
                .map(Student_Class::getClassEntity)
                .map(ClassEntity::getId)
                .toList();
        return classRepository.findAllByStatusIgnoreCase("planning")
                .stream()
                .filter(cls -> !enrolledID.contains(cls.getId()))
                .map(ClassEntityMapper::mapToDTO)
                .toList();
    }

    @Override
    public Boolean addReservedClassFromList(String id, ReservedClassDTO reservedClass) {
        var size = reserveClassRepository.countAllByStudent_Id(id);

        return (addReservedClass(id, reservedClass).size() - 1) == size;
    }

    @Override
    public List<ReservedClassDTO> addReservedClass(String id, ReservedClassDTO reservedClass) {
        if (reserveClassRepository.existsByClassEntity_IdAndStudent_Id(reservedClass.getClassID(), id))
            throw new IllegalArgumentException("Class " + reservedClass.getClassID() + " is enrolled");

        var studentClassData = studentClassRepository
                .findByStudentIdAndClassEntityId(id, reservedClass.getClassID())
                .orElseThrow(() ->
                        new NoSuchElementException("Student is not in class " + reservedClass.getClassID())
                );


        if (!studentClassData.getClassEntity().getStatus().equalsIgnoreCase("Planning"))
            throw new IllegalArgumentException("Class \"" + reservedClass.getClassID() + "\" is not in planning status");

        studentClassData.setAttendingStatus("reserve");

        studentClassRepository.save(studentClassData);

        var cls = ReservedClassMapper.mapToEntity(reservedClass, studentClassData.getClassEntity(), studentClassData.getStudent());

        reserveClassRepository.save(cls);

        return reserveClassRepository.findAllByStudent_Id(id)
                .stream()
                .map(ReservedClassMapper::mapToDTO).toList();
    }


    @Override
    public List<ReservedClassViewDTO> fetchReservableClass(String id) {
        if (!studentRepository.existsById(id)) {
            throw new NoSuchElementException("No student with id \"" + id + "\" found");
        }
        var reserveClassID = reserveClassRepository.findAllByStudent_Id(id)
                .stream()
                .map(cls -> cls.getClassEntity().getId())
                .toList();
        return studentClassRepository
                .findAllByStudentIdAndClassEntity_StatusIgnoreCase(id, "Planning")
                .stream()
                .filter(cls -> !reserveClassID.contains(cls.getClassEntity().getId()))
                .map(ReservedClassViewMapper::mapToDTO)
                .toList();
    }

    @Override
    public List<ReservedClassDTO> fetchReservedClass(String id) {
        return reserveClassRepository.findAllByStudent_Id(id)
                .stream()
                .map(ReservedClassMapper::mapToDTO).toList();
    }
}
