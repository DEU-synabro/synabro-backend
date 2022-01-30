package com.deu.synabro.http.request;

import lombok.Data;

@Data
public class SignUpRequest {
    private String userId;
    private String email;
    private String password;
    private String username;
}
