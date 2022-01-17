package com.deu.synabro.board.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.*;
import java.time.LocalDateTime;


@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="board")
@EntityListeners(AuditingEntityListener.class)
public class BoardEntity{

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
    @Column
    @CreatedDate
    private LocalDateTime created_date;
    @Column
    @LastModifiedDate
    private LocalDateTime updated_date;

    public BoardEntity(String title, String contents, BoardType boardType) {
        this.title = title;
        this.contents = contents;
        this.boardType = boardType;
    }

    @Builder
    public BoardEntity(String userid, String title, String contents) {
        this.userid = userid;
        this.title = title;
        this.contents = contents;
    }
}
