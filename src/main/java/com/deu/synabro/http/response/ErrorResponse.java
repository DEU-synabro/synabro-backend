package com.deu.synabro.http.response;

import lombok.Getter;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public class ErrorResponse {
    private int code;
    private String message;

    public static ErrorResponse of(HttpStatus httpStatus, String message) {
        return new ErrorResponse(httpStatus.value(), message);
    }
}
