package com.deu.synabro.http.request.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "회원가입")
public class SignUpRequest {
    @Schema(description = "사용자 이메일", nullable = false, example = "volunteer@synabro.com")
    private String email;
    @Schema(description = "사용자 암호", nullable = false, example = "volunteer")
    private String password;
    @Schema(description = "사용자 이름", nullable = false, example = "하지민")
    private String username;
}
