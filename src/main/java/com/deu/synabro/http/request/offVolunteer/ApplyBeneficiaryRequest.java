package com.deu.synabro.http.request.offVolunteer;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ApplyBeneficiaryRequest extends BeneficiaryRequest{
    @Schema(description = "주소",example = "제목")
    private String address;
    @Schema(description = "소속",example = "제목")
    private String teamGroup;
    @Schema(description = "전화번호",example = "제목")
    private String phone;

    public ApplyBeneficiaryRequest(String name, String password, String address, String teamGroup, String phone){
        super(name, password);
        this.address = address;
        this.teamGroup = teamGroup;
        this.phone = phone;
    }
}
