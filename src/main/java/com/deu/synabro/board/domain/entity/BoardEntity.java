package com.deu.synabro.board.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name="board")
public class BoardEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private String writer;

    @Builder
    public BoardEntity(Long idx, String title, String content, String writer) {
        this.idx = idx;
        this.title = title;
        this.content = content;
        this.writer = writer;
    }
}
