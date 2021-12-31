package com.deu.synabro.board.dto;

import com.deu.synabro.board.domain.entity.BoardEntity;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardDTO {
    private Long idx;
    private String title;
    private String content;
    private String writer;

    public BoardEntity toEntity(){
        BoardEntity boardEntity = BoardEntity.builder()
                .idx(idx)
                .writer(writer)
                .title(title)
                .content(content)
                .build();
        return boardEntity;
    }

    @Builder
    public BoardDTO(Long idx, String title, String content, String writer) {
        this.idx = idx;
        this.title = title;
        this.content = content;
        this.writer = writer;
    }


}
