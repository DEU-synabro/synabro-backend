package com.deu.synabro.board.dto;

import com.deu.synabro.board.domain.BoardEntity;
import com.deu.synabro.board.domain.BoardType;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardDTO {
    private Long id;
    private String userid;
    private BoardType type;
    private String title;
    private String contents;
    private String created_date;
    private String updated_date;

    public BoardEntity toEntity(){
        BoardEntity boardEntity = BoardEntity.builder()
                .userid(userid)
                .title(title)
                .contents(contents)
                .build();
        return boardEntity;
    }
}
