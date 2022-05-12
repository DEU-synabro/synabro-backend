package com.deu.synabro.http.response.education;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Schema(description = "교육홈을 조회할 때 사용되는 교육 목록 응답 구조 ")
@Getter @RequiredArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EducationResponse {
    @Schema(description = "교육글 고유번호", example = "8857ba20-2cb7-407e-908c-b333cf1257c5")
    private final UUID id;

    @Schema(description = "교육글 고유번호", example = "제목글, 바닥글 적기")
    private final String title;

    @Schema(description = "교육 진행 상태", example = "PROGRESS")
    private final String status;
}
