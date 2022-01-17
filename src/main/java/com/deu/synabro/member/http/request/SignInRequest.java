package com.deu.synabro.member.http.request;

import lombok.Data;

@Data
public class SignInRequest {
    private String userId;
    private String password;
}
