package com.deu.synabro.http.request;

import com.deu.synabro.entity.Board;
import com.deu.synabro.entity.BoardType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BoardRequest {

    @Schema(description = "유저 아이디",example = "abc123")
    private String userid;
    @Schema(description = "글 제목",example = "글 제목")
    private String title;
    @Schema(description = "글 내용",example = "글 내용")
    private String contents;
    @Schema(description = "게시판 종류",example = "종류")
    private BoardType boardType;

    public Board toEntity(){
        Board boardEntity = Board.builder()
                .userid(userid)
                .title(title)
                .contents(contents)
                .boardType(boardType)
                .build();
        return boardEntity;
    }
}
