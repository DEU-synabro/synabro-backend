package com.deu.synabro.http.response;

import com.deu.synabro.entity.Board;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter @Schema(description = "게시판")
@RequiredArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BoardSimpleResponse {
    @Schema(description = "게시글 고유번호", example = "8857ba20-2cb7-407e-908c-b333cf1257c5")
    private final UUID id;

    @Schema(description = "게시판 제목", example = "[ 필 독 ] 교육 첫 시작시 주의사항")
    private final String title;

    public BoardSimpleResponse(Board board) {
        this.id = board.getIdx();
        this.title = board.getTitle();
    }
}
