package com.deu.synabro.http.request.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
public class SignInRequest {

    @NotNull
    @Email
    @Schema(description = "사용자 이메일", nullable = false, example = "volunteer@synabro.com")
    private String email;

    @NotNull
    @Size(min = 3, max = 100)
    @Schema(description = "사용자 비밀번호", nullable = false, example = "volunteer")
    private String password;
}
