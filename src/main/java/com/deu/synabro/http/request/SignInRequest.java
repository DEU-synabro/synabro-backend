package com.deu.synabro.http.request;

import lombok.Data;

@Data
public class SignInRequest {
    private String userId;
    private String password;
}
