package com.deu.synabro.board.dto;

import com.deu.synabro.board.domain.entity.BoardEntity;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardDTO {
    private Long id;
    private String userid;
    private String type;
    private String title;
    private String contents;
    private String created_date;
    private String updated_date;

    public BoardEntity toEntity(){
        BoardEntity boardEntity = BoardEntity.builder()
                .userid(userid)
                .type(type)
                .title(title)
                .contents(contents)
                .build();
        return boardEntity;
    }

    @Builder
    public BoardDTO(String userid, String type, String title, String contents) {
        this.userid = userid;
        this.type = type;
        this.title = title;
        this.contents = contents;
    }
}
