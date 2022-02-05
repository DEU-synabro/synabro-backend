package com.deu.synabro.http.response;

import com.deu.synabro.entity.Board;
import com.deu.synabro.entity.enums.BoardType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Schema(description = "게시판")
@NoArgsConstructor
public class BoardResponse {
    @Schema(description = "고유번호", example = "1")
    private Long id;

    @Schema(description = "아이디", example = "dnd01")
    private String userid;

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
