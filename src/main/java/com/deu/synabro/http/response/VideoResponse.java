package com.deu.synabro.http.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Schema(description = "비디오")
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class VideoResponse {
    @Schema(description = "아이디", example = "dnd02")
    private String workId;
    @Schema(description = "url", example = "url")
    private String url;
    @Schema(description = "제목", example = "제목")
    private String fileName;
}
