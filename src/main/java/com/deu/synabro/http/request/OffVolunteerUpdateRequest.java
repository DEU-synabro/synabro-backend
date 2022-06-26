package com.deu.synabro.http.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OffVolunteerUpdateRequest {

    @Schema(description = "봉사 제목",example = "봉사 제목")
    String title;

    @Schema(description = "봉사 내용",example = "봉사 내용")
    String contents;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "봉사 모집 시작 기간", example = "2022-02-12T12:16:40.77")
    LocalDateTime startPeriod;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "봉사 모집 마감 기간", example = "2022-02-12T12:16:40.77")
    LocalDateTime endPeriod;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "봉사 수행 시작 날짜", example = "2022-02-12T12:16:40.77")
    LocalDateTime startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "봉사 모집 종료 날짜", example = "2022-02-12T12:16:40.77")
    LocalDateTime endDate;
}
