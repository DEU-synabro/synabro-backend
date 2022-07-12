package com.deu.synabro.http.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 *  페이징 처리를 위한 정보를 담는 클래스
 */
@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BoardListResponse {
    @Schema(description = "고유번호", example = "8857ba20-2cb7-407e-908c-b333cf1257c5")
    private UUID idx;

    @Schema(description = "게시판 제목", example = "제목")
    private String title;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "Asia/Seoul")
    @Schema(description = "게시판 생성 날짜", example = "2022-04-07")
    private LocalDateTime createdDate;

    public static void addNullBoardListResponse(List<BoardListResponse> boardListResponseList){
        BoardListResponse boardListResponse = BoardListResponse.builder()
                .idx(null)
                .title(null)
                .createdDate(null)
                .build();
        boardListResponseList.add(boardListResponse);
    }
}
