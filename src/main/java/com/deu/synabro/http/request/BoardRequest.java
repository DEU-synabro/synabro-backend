package com.deu.synabro.http.request;

import com.deu.synabro.entity.Board;
import com.deu.synabro.entity.enums.BoardType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;

/**
 *  게시글 생성 요청 정보를 담는 크래스
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BoardRequest {
    
    @Schema(description = "글 제목",example = "제목")
    private String title;
    @Schema(description = "글 내용",example = "내용")
    private String contents;
    @Enumerated(value= EnumType.STRING)
    @Schema(description = "게시판 종류")
    private BoardType boardType;

    public BoardRequest(String title, String contents, BoardType boardType) {
        this.title = title;
        this.contents = contents;
        this.boardType = boardType;
    }

    public Board toEntity(BoardRequest boardRequest){
        Board board = Board.builder()
                .title(boardRequest.getTitle())
                .contents(boardRequest.getContents())
                .boardType(boardRequest.getBoardType())
                .build();
        return board;
    }
}
