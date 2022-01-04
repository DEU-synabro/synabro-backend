package com.deu.synabro.board.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
@Table(name="board")
public class BoardEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="user_id")
    private String userid;
    @Column
    private String type;
    @Column
    private String title;
    @Column
    private String contents;
    @Column
    private LocalDateTime created_date = LocalDateTime.now();
    @Column
    private LocalDateTime updated_date;

    @Builder
    public BoardEntity(String userid, String type, String title, String contents) {
        this.userid = userid;
        this.type = type;
        this.title = title;
        this.contents = contents;
    }
}
