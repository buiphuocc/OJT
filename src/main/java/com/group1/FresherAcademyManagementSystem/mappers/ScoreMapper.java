package com.group1.FresherAcademyManagementSystem.mappers;

import com.group1.FresherAcademyManagementSystem.dtos.ScoreDto;
import com.group1.FresherAcademyManagementSystem.dtos.ScoreDto2;
import com.group1.FresherAcademyManagementSystem.models.Score;

public class ScoreMapper {

    public static ScoreDto mapToDto(Score score) {
        return ScoreDto.builder()
                .id(score.getId())
                .score(score.getScore())
                .scoreName(score.getScoreName())
                .build();
    }



    public static Score mapToEntity(ScoreDto scoreDto){
        return Score.builder()
                .id(scoreDto.getId())
                .score(scoreDto.getScore())
                .scoreName(scoreDto.getScoreName())
                .build();
    }

    public static Score mapToEntity2(ScoreDto2 scoreDto){

        return Score.builder()
                .id(scoreDto.getId())
                .score(scoreDto.getScore())
                .build();
    }
}
