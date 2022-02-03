package com.deu.synabro.http.response;

import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;


@Getter @Setter
public class ErrorResponse {
    @Schema(description = "에러 메시지", defaultValue = "")
    private String errorMessage;

    @Schema(description = "에러코드", allowableValues = {"404", "500"})
    private String errorCode;

    public ErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
        this.errorCode = "404";
    }
    public ErrorResponse(String errorMessage, String errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }
}
