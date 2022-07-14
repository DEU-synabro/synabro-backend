package com.deu.synabro.http.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class CertificationRequest {
    @Schema(description = "제목",example = "제목")
    String title;

    @Schema(description = "내용",example = "내용")
    String contents;
}
