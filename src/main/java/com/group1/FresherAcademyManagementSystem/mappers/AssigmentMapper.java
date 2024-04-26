package com.group1.FresherAcademyManagementSystem.mappers;

import com.group1.FresherAcademyManagementSystem.dtos.AssignmentDto;
import com.group1.FresherAcademyManagementSystem.dtos.ScoreDto;
import com.group1.FresherAcademyManagementSystem.models.Assignment;
import com.group1.FresherAcademyManagementSystem.models.Score;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AssigmentMapper {
    public static AssignmentDto mapToDto(Assignment assignment, String studentId){

        List< ScoreDto> scoreDtos = new ArrayList<>();
        for (Score x : assignment.getScores()) {
            if(x.getStudent().getId().equalsIgnoreCase(studentId)){
                ScoreDto s = ScoreMapper.mapToDto(x);
                scoreDtos.add(s);
            }
        }
        return AssignmentDto.builder()
                .assignmentName(assignment.getAssignmentName())
                .assignmentScore(scoreDtos)
                .build();
    }
    public static AssignmentDto mapToDto2(Assignment assignment){
        return AssignmentDto.builder()
                .assignmentName(assignment.getAssignmentName())
                .assignmentScore( assignment.getScores().stream().map(item -> ScoreMapper.mapToDto(item)).collect(Collectors.toList()))
                .build();
    }

    public static Assignment mapToEntity(AssignmentDto assignmentDto) {
        return Assignment.builder()
                .id(assignmentDto.getId())
                .assignmentName(assignmentDto.getAssignmentName())
                .scores(assignmentDto.getAssignmentScore().stream().map(item -> ScoreMapper.mapToEntity(item)).collect(Collectors.toList()))
                .build();
    }

    public static Assignment mapToEntity2(AssignmentDto assignmentDto) {
        return Assignment.builder()
                .id(assignmentDto.getId())
                .assignmentName(assignmentDto.getAssignmentName())
                .scores(assignmentDto.getAssignmentScore().stream().map(item -> ScoreMapper.mapToEntity(item)).collect(Collectors.toList()))
                .build();
    }
}
