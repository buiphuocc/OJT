package com.group1.FresherAcademyManagementSystem.services.Impl;


import com.group1.FresherAcademyManagementSystem.dtos.ClassEntityDTO;
import com.group1.FresherAcademyManagementSystem.dtos.ClassForReserveDto;
import com.group1.FresherAcademyManagementSystem.dtos.ReserveClassListDto;
import com.group1.FresherAcademyManagementSystem.dtos.reserveClassDTO;
import com.group1.FresherAcademyManagementSystem.mappers.ClassEntityMapper;
import com.group1.FresherAcademyManagementSystem.mappers.ReserveClassMapper;
import com.group1.FresherAcademyManagementSystem.models.ClassEntity;
import com.group1.FresherAcademyManagementSystem.models.Reserved_Class;
import com.group1.FresherAcademyManagementSystem.models.Student;
import com.group1.FresherAcademyManagementSystem.models.Student_Class;
import com.group1.FresherAcademyManagementSystem.repositories.ClassRepository;
import com.group1.FresherAcademyManagementSystem.repositories.ReserveClassRepository;
import com.group1.FresherAcademyManagementSystem.repositories.StudentClassRepository;
import com.group1.FresherAcademyManagementSystem.services.ReserveClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReserveClassServiceImpl implements ReserveClassService {

    private final ReserveClassRepository reserveClassRepository;

    private final StudentClassRepository studentClassRepository;

    private final ClassRepository classRepository;


    @Override
    public reserveClassDTO addStudentByReserve(reserveClassDTO newStudentDetails) {
        // Tạo một đối tượng Reserved_Class từ reserveClassDto
        Reserved_Class newStudent = new Reserved_Class();


        newStudent.setId(newStudentDetails.getId());
        newStudent.setStartDate(newStudentDetails.getStartDate());
        newStudent.setEndDate(newStudentDetails.getEndDate());
        newStudent.setReasonReverse(newStudentDetails.getReasonReverse());

        // Tạo một đối tượng ClassEntity và thiết lập thông tin nếu cần
        ClassEntity newClassEntity = new ClassEntity();
        newClassEntity.setId(newStudentDetails.getClassEntity().getId());
        newStudent.setClassEntity(newClassEntity);

        // Tạo một đối tượng Student và thiết lập thông tin nếu cần
        Student newStudentEntity = new Student();
        newStudentEntity.setId(newStudentDetails.getStudent().getId());
        newStudent.setStudent(newStudentEntity);

        // Lưu đối tượng mới vào cơ sở dữ liệu
        newStudent = reserveClassRepository.save(newStudent);

        // Chuyển đổi đối tượng mới thành DTO và trả về
        return ReserveClassMapper.mapToDto(newStudent);
    }

    @Override
    public reserveClassDTO updateStudentByReserve(Long Id, reserveClassDTO updatedStudentDetails) {
        Reserved_Class existingStudent = reserveClassRepository.findById(Id)
                .orElse(null);

        if (existingStudent != null) {
            existingStudent.setId(updatedStudentDetails.getId());
            existingStudent.setEndDate(updatedStudentDetails.getEndDate());
            existingStudent.setStartDate(updatedStudentDetails.getStartDate());
            existingStudent.setReasonReverse(updatedStudentDetails.getReasonReverse());

            // Truy cập ClassEntity và thiết lập classId
            if (existingStudent.getClassEntity() != null) {
                existingStudent.getClassEntity().setId(updatedStudentDetails.getClassEntity().getId());
            }

            // Truy cập Student và thiết lập id
            if (existingStudent.getStudent() != null) {
                existingStudent.getStudent().setId(updatedStudentDetails.getStudent().getId());
            }

            // Lưu thay đổi vào cơ sở dữ liệu hoặc thực hiện các bước khác cần thiết
            existingStudent = reserveClassRepository.save(existingStudent);

            return ReserveClassMapper.mapToDto(existingStudent);
        }

        return null;
    }

    @Override
    public void removeStudentFromReserve(Long id) {
        Optional<Reserved_Class> removeStudentOptional = reserveClassRepository.findById(id);
        if (removeStudentOptional.isPresent()) {
            Reserved_Class removeStudent = removeStudentOptional.get();
            List<Student_Class> listStudentInClass = studentClassRepository.findByStudentIdAndClassId(removeStudent.getStudent().getId(),removeStudent.getClassEntity().getId());
            Student_Class studentClass = listStudentInClass.get(0);
            studentClass.setAttendingStatus("inclass");

            studentClassRepository.save(studentClass);
            reserveClassRepository.delete(removeStudent);
        } else {
            throw new NoSuchElementException("Student with ID " + id + " not found in reservation list");
        }

    }

    @Override
    public Page<ReserveClassListDto> reservedClassSearchByFilter(int pageNumber, int pageSize, String[] listFilter, String sortDir, String keyword) {
        //Define boolean check field for query
        boolean id = false;
        boolean fullName = false;
        boolean studentId = false;
        boolean classId = false;

        if(listFilter!=null){
            for (String filterString:listFilter){
                if(filterString.equals("id")) id=true;
                if(filterString.equals("fullName")) fullName =true;
                if(filterString.equals("studentId")) studentId = true;
                if(filterString.equals("classId")) classId = true;
            }
        }

        //Pageable for query
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        //Page list of Dtos
        Page<ReserveClassListDto> resultlistDto = null;

        //Based on the keyword field we'll have different action for getting page list
        if (keyword != null) {
            //Get page list from db
            Page<Reserved_Class> resultList = reserveClassRepository.findByIdOrClassNameOrStatus(keyword, pageable, id, fullName, studentId, classId);
            //Map above list to page list of dtos
            resultlistDto = resultList.map(new Function<Reserved_Class, ReserveClassListDto>() {
                @Override
                public ReserveClassListDto apply(Reserved_Class resultList) {
                    return new ReserveClassListDto(
                            resultList.getId(),
                            resultList.getStudent().getFullName(),
                            resultList.getStudent().getId(),
                            resultList.getStudent().getGender(),
                            resultList.getStudent().getDob(),
                            new ClassForReserveDto(resultList.getClassEntity().getId(), resultList.getClassEntity().getClassName()),
                            resultList.getReasonReverse(),
                            resultList.getStartDate(),
                            resultList.getEndDate()
                    );
                }
            });
        }else {
            Page<Reserved_Class> resultList = reserveClassRepository.findAll(pageable);
            resultlistDto = resultList.map(new Function<Reserved_Class, ReserveClassListDto>() {
                @Override
                public ReserveClassListDto apply(Reserved_Class resultList) {
                    return new ReserveClassListDto(
                            resultList.getId(),
                            resultList.getStudent().getFullName(),
                            resultList.getStudent().getId(),
                            resultList.getStudent().getGender(),
                            resultList.getStudent().getDob(),
                            new ClassForReserveDto(resultList.getClassEntity().getId(), resultList.getClassEntity().getClassName()),
                            resultList.getReasonReverse(),
                            resultList.getStartDate(),
                            resultList.getEndDate()
                    );
                }
            });
        }

        //Convert Page list of dtos back to a normal list
        List<ReserveClassListDto> contentList = resultlistDto.getContent();
        //Define sorting list
        List<ReserveClassListDto> sortedContent = contentList.stream()
                .sorted((dto1, dto2) -> {
                    if ("asc".equals(sortDir)) {
                        return dto1.getId().compareTo(dto2.getId());
                    } else {
                        return dto2.getId().compareTo(dto1.getId());
                    }
                })
                .collect(Collectors.toList());

        //Sorting above list based on requirements
        Pageable pageableDto = PageRequest.of(pageNumber - 1, pageSize, Sort.by(listFilter).ascending());
        Page<ReserveClassListDto> sortedList = new PageImpl<>(sortedContent, pageableDto, resultlistDto.getTotalElements());
        return sortedList;
    }

    public List<ClassForReserveDto> getClassWithStudentId(String studentId){
        List<Reserved_Class> listReserveClass = reserveClassRepository.findAllByStudent_Id(studentId);
        List<ClassEntity> listClass = new ArrayList<>();
        for (Reserved_Class reservedClass : listReserveClass){
            listClass.add(reservedClass.getClassEntity());
        }
        return listClass.stream().map(classEntity -> ClassEntityMapper.mapToReserveClassDto(classEntity)).collect(Collectors.toList());
    }
    
    @Override
    public List<Reserved_Class> getAllReservedStudentsForRemind() {
        return reserveClassRepository.findAll();
    }

}
