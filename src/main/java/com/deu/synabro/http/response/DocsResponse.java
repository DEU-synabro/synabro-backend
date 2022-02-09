package com.deu.synabro.http.response;

import com.deu.synabro.entity.enums.BoardType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(description = "첨부파일")
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DocsResponse {

    @Schema(description = "아이디", example = "dnd02")
    private String wordId;

    @Schema(description = "내용", example = "내용")
    private String contents;

    @Schema(description = "페이지", example = "페이지 수")
    private String page;

    @Schema(description = "제목", example = "제목")
    private String fileName;


}
