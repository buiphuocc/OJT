package com.group1.FresherAcademyManagementSystem.services.Impl;

import com.group1.FresherAcademyManagementSystem.dtos.ClassListDto;
import com.group1.FresherAcademyManagementSystem.dtos.ClassWithStudentInClassDto;
import com.group1.FresherAcademyManagementSystem.dtos.StudentByClassDto;
import com.group1.FresherAcademyManagementSystem.exceptions.NotFoundException;
import com.group1.FresherAcademyManagementSystem.mappers.ClassEntityMapper;
import com.group1.FresherAcademyManagementSystem.models.ClassEntity;
import com.group1.FresherAcademyManagementSystem.repositories.ClassRepository;
import com.group1.FresherAcademyManagementSystem.services.ClassService;
import com.group1.FresherAcademyManagementSystem.services.StudentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@CacheConfig(cacheNames = "class")
@Service
public class ClassServiceImpl implements ClassService {

    private final ClassRepository classRepository;

    private final StudentService studentService;

    private final ModelMapper modelMapper;

    @Autowired
    public ClassServiceImpl(ClassRepository theClassRepository, StudentService studentService, ModelMapper modelMapper) {
        this.classRepository = theClassRepository;
        this.studentService = studentService;
        this.modelMapper = modelMapper;
    }

    @Override
    public ClassWithStudentInClassDto findClassAndStudentById(String classId) {
        ClassEntity clazz = classRepository.findById(classId).orElseThrow(() -> new NotFoundException("Class " + classId + " Not Found"));

        List<StudentByClassDto> students = studentService.findByStudentByClass(classId);

        ClassWithStudentInClassDto classWithStudentInClassDto = modelMapper.map(clazz, ClassWithStudentInClassDto.class);
        classWithStudentInClassDto.setProgramName(clazz.getTrainingProgram().getName());
        classWithStudentInClassDto.setStudents(students.stream()
                .map(student -> modelMapper.map(student, StudentByClassDto.class))
                .collect(Collectors.toList()));

        return classWithStudentInClassDto;
    }

    @Override
    public List<ClassListDto> getAllClass() {
        return classRepository.findAll().stream().map(ClassEntityMapper::mapToClassListDto).collect(Collectors.toList());
    }

    @Override
    public Page<ClassEntity> getClassSearchByFilter(int pageNumber, int pageSize, String sortField, String sortDir, String keyword, String[] viewBy) {
        Sort sortClass = Sort.by(sortField);
        sortClass = sortDir.equals("asc") ? sortClass.ascending() : sortClass.descending();

        boolean inProgress = false;
        boolean finished = false;


        if (viewBy != null) {
            for (String viewByString : viewBy) {
                if (viewByString.equals("inprogress")) inProgress = true;
                if (viewByString.equals("finished")) finished = true;
            }
        }

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sortClass);

        if (keyword != null)
            return classRepository.findByIdOrClassNameOrStatus(keyword, pageable, inProgress, finished);
        else if (inProgress || finished) {
            return classRepository.findByStatus(pageable, inProgress, finished);
        }
        return classRepository.findAll(pageable);
    }


}
