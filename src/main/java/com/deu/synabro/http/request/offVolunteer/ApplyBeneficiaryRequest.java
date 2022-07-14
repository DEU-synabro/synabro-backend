package com.deu.synabro.http.request.offVolunteer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ApplyBeneficiaryRequest extends BeneficiaryRequest{

    @Schema(description = "글 제목",example = "제목")
    private String teamGroup;
    @Schema(description = "글 제목",example = "제목")
    private String phone;

    public ApplyBeneficiaryRequest(String name, String password, String teamGroup, String phone){
        super(name, password);
        this.teamGroup = teamGroup;
        this.phone = phone;
    }
}
