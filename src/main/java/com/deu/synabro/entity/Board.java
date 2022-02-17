package com.deu.synabro.entity;

import com.deu.synabro.entity.enums.BoardType;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.UUID;


@RequiredArgsConstructor
@Getter
@Setter
@Entity
@Table(name="board")
@EntityListeners(AuditingEntityListener.class)
public class Board extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "board_id", columnDefinition = "BINARY(16)")
    private UUID id;
    @Column(name="user_id")
    private String userId;
    @Column(name="title")
    private String title;
    @Column(name="contents")
    private String contents;
    @Enumerated(value=EnumType.STRING)
    @Column(name="boardtype")
    private BoardType boardType;

    @Builder
    public Board(String userId, String title, String contents, BoardType boardType) {
        this.userId = userId;
        this.title = title;
        this.contents = contents;
        this.boardType = boardType;
    }
}
