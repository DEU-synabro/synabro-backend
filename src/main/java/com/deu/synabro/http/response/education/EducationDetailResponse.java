package com.deu.synabro.http.response.education;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.UUID;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EducationDetailResponse extends EducationResponse {

    @Schema(description = "교육글 설명", example = "제목글, 바닥글 적는 훈련을 진행합니다.")
    private final String description;

    @Schema(description = "파일 다운로드 링크", example = "http://localhost:8080/education/downloads/8857ba20-2cb7-407e-908c-b333cf1257c5")
    private final String file;

    public EducationDetailResponse() {
        super(UUID.randomUUID(), "제목글, 바닥글 적기", "PROGRESS");
        this.description = "제목글, 바닥글 적는 훈련을 진행합니다.";
        this.file = "https://www.os-book.com/OS10/slide-dir/PPTX-dir/ch1.pptx";
    }
}
