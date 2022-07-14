package com.deu.synabro.http.request.offVolunteer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class BeneficiaryRequest {

    @Schema(description = "글 제목",example = "제목")
    private String name;
    @Schema(description = "글 제목",example = "제목")
    private String password;

    @Builder
    public BeneficiaryRequest(String name, String password) {
        this.name = name;
        this.password = password;
    }
}
