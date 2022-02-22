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

    @Schema(description = "파일 이름", example = "파일 이름")
    private String fileName;


}
