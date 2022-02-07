package com.deu.synabro.entity;

import com.deu.synabro.entity.enums.BoardType;
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
    @Column(name="userid")
    private String userid;
    @Column(name="title")
    private String title;
    @Column(name="contents")
    private String contents;
    @Enumerated(value=EnumType.STRING)
    @Column(name="boardtype")
    private BoardType boardtype;

    @Builder
    public Board(String userid, String title, String contents, BoardType boardtype) {
        this.userid = userid;
        this.title = title;
        this.contents = contents;
        this.boardtype = boardtype;
    }
}
