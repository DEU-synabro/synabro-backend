package com.deu.synabro.entity;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;


@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="board")
@EntityListeners(AuditingEntityListener.class)
public class Board extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="user_id")
    private String userid;
    @Column
    private String title;
    @Column
    private String contents;
    @Column
    private BoardType boardType;

    @Builder
    public Board(String userid, String title, String contents, BoardType boardType) {
        this.userid = userid;
        this.title = title;
        this.contents = contents;
        this.boardType = boardType;
    }
}
