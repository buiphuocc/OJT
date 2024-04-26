package com.group1.FresherAcademyManagementSystem.services;

import com.group1.FresherAcademyManagementSystem.dtos.ClassListDto;
import com.group1.FresherAcademyManagementSystem.dtos.ClassWithStudentInClassDto;
import com.group1.FresherAcademyManagementSystem.models.ClassEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ClassService {
    ClassWithStudentInClassDto findClassAndStudentById(String classId);

    List<ClassListDto> getAllClass();

    Page<ClassEntity> getClassSearchByFilter(int pageNumber, int pageSize, String sortField, String sortDir, String keyword, String[] viewByStatus);
}
