package com.deu.synabro.http.response;

import com.deu.synabro.entity.Board;
import com.deu.synabro.entity.enums.BoardType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@Schema(description = "게시판")
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BoardResponse {
    @Schema(description = "고유번호", example = "8857ba20-2cb7-407e-908c-b333cf1257c5")
    private UUID id;

    @Schema(description = "아이디", example = "dnd01")
    private String userId;

    @Schema(description = "게시판 종류", example = "종류")
    private BoardType boardType;

    @Schema(description = "게시판 제목", example = "제목")
    private String title;

    @Schema(description = "게시판 내용", example = "내용")
    private String contents;

    @Schema(description = "게시판 생성 날짜")
    private LocalDateTime created_date;

    @Schema(description = "게시판 수정 날짜")
    private LocalDateTime updated_date;
}