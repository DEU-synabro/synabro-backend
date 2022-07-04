package com.deu.synabro.http.response;

import com.deu.synabro.entity.Board;
import com.deu.synabro.entity.enums.BoardType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Schema(description = "게시판")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BoardResponse extends BoardListResponse{
    @Schema(description = "게시판 내용", example = "내용")
    private String contents;

    BoardResponse(UUID idx, String title, LocalDateTime createdDate) {
        super(idx, title, createdDate);
    }
}