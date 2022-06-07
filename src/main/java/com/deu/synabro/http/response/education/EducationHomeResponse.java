package com.deu.synabro.http.response.education;

import com.deu.synabro.entity.enums.EducationType;
import com.deu.synabro.http.response.BoardSimpleResponse;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Schema(description = "교육홈을 조회할 때 사용되는 응답 ")
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EducationHomeResponse {
    List<BoardSimpleResponse> board;
    List<EducationResponse> education;
    Integer progress;

    public EducationHomeResponse(List<BoardSimpleResponse> board) {
        this.board = board;
        List<EducationResponse> educationResponses = new ArrayList<>();

        // Education Sample data
        educationResponses.add(new EducationResponse(UUID.randomUUID(), "제목글, 바닥글 적기", EducationType.PROGRESS.getValue()));
        educationResponses.add(new EducationResponse(UUID.randomUUID(), "인용문 작성하기", EducationType.TODO.getValue()));
        educationResponses.add(new EducationResponse(UUID.randomUUID(), "지문 작성하기", EducationType.TODO.getValue()));
        educationResponses.add(new EducationResponse(UUID.randomUUID(), "동영상 자막 작성하기", EducationType.TODO.getValue()));
        educationResponses.add(new EducationResponse(UUID.randomUUID(), "도서 자료 전자화하기", EducationType.DONE.getValue()));
        educationResponses.add(new EducationResponse(UUID.randomUUID(), "문서 자료 전자화하기", EducationType.DONE.getValue()));

        this.education = educationResponses;
        this.progress = 63;
    }
}
