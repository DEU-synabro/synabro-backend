package com.deu.synabro.http.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OffVolunteerResponse {
    @Schema(description = "봉사 제목", example = "제목")
    private String title;

    @Schema(description = "고유번호", example = "8857ba20-2cb7-407e-908c-b333cf1257c5")
    private ArrayList<UUID> docsId;

    @Schema(description = "봉사 설명", example = "설명")
    private String contents;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "봉사 모집 시작 날짜")
    private LocalDateTime startPeriod;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "봉사 모집 종료 날짜")
    private LocalDateTime endPeriod;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "봉사 시작 날짜")
    private LocalDateTime startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "봉사 종료 날짜")
    private LocalDateTime endDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "봉사 글 생성 날짜")
    private LocalDateTime createdDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "봉사 글 수정 날짜")
    private LocalDateTime updatedDate;

}
