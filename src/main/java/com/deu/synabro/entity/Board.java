package com.deu.synabro.entity;

import com.deu.synabro.entity.enums.BoardType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
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
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Board extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "board_id", columnDefinition = "BINARY(16)")
    @Schema(description = "고유번호", example = "8857ba20-2cb7-407e-908c-b333cf1257c5")
    private UUID id;

    @Column(name="title")
    @Schema(description = "게시판 제목", example = "제목")
    private String title;

    @Column(name="contents")
    @Schema(description = "게시판 내용", example = "내용")
    private String contents;

    @Enumerated(value=EnumType.STRING)
    @Column(name="board_type")
    @Schema(description = "게시판 종류")
    private BoardType boardType;

    @Builder
    public Board(String title, String contents, BoardType boardType) {
        this.title = title;
        this.contents = contents;
        this.boardType = boardType;
    }
}
