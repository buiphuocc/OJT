package com.group1.FresherAcademyManagementSystem.services;

import com.group1.FresherAcademyManagementSystem.dtos.ReserveClassListDto;
import com.group1.FresherAcademyManagementSystem.dtos.reserveClassDTO;
import com.group1.FresherAcademyManagementSystem.models.Reserved_Class;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ReserveClassService {
    reserveClassDTO addStudentByReserve(reserveClassDTO newStudentDetails);

    reserveClassDTO updateStudentByReserve(Long studentId, reserveClassDTO updatedStudentDetails);

    void removeStudentFromReserve(Long id);   
    
    List<Reserved_Class> getAllReservedStudentsForRemind();

    Page<ReserveClassListDto> reservedClassSearchByFilter(int pageNumber, int pageSize, String[] listFilter, String sortDir, String keyword);
}
