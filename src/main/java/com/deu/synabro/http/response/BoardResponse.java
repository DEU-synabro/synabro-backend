package com.deu.synabro.http.response;

import com.deu.synabro.entity.enums.BoardType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BoardResponse {
    @Schema(description = "고유번호", example = "8857ba20-2cb7-407e-908c-b333cf1257c5")
    private UUID id;

    @Schema(description = "고유번호", example = "8857ba20-2cb7-407e-908c-b333cf1257c5")
    private  ArrayList<UUID> docsId;

    @Schema(description = "봉사 제목", example = "제목")
    private String title;

    @Schema(description = "봉사 설명", example = "설명")
    private String contents;

    @Schema(description = "봉사 설명", example = "설명")
    private BoardType boardType;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "봉사 생성 날짜")
    private LocalDateTime createdDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "봉사 수정 날짜")
    private LocalDateTime updatedDate;
}
