package com.deu.synabro.http.response;

import lombok.Data;
import org.springframework.http.HttpEntity;

@Data
public class SingInResponse extends HttpEntity {
    private final Object status;
    private String message;
}
