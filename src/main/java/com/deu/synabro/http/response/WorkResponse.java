package com.deu.synabro.http.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

/**
 * 봉사 요청 정보를 반환시에 사용되는 클래스 입니다
 *
 * @author tkfdkskarl56
 * @since 1.0
 */
@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WorkResponse {
    @Schema(description = "고유번호", example = "8857ba20-2cb7-407e-908c-b333cf1257c5")
    private UUID id;

    @Schema(description = "아이디", example = "62ddd3ba-bbf0-469f-9c7d-f908df979c75")
    private UUID userId;

    @Schema(description = "고유번호", example = "8857ba20-2cb7-407e-908c-b333cf1257c5")
    private ArrayList<UUID> docsId;

    @Schema(description = "봉사 제목", example = "제목")
    private String title;

    @Schema(description = "봉사 설명", example = "설명")
    private String contents;

    @Schema(description = "봉사 시간", example = "56")
    private Short volunteerTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "봉사 생성 날짜")
    private LocalDateTime createdDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "봉사 수정 날짜")
    private LocalDateTime updatedDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "봉사 종료 날짜")
    private LocalDateTime endedDate;

}
