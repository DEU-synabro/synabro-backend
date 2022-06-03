package com.deu.synabro.http.request.member;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MemberPatchRequest {

    @Schema(description = "사용자 주소", example = "부산광역시 부산진구 시나브로 12 봉사아파트 802호")
    private String introduction;

    @Schema(description = "사용자 이름", example = "하지민")
    private String username;

    @Schema(description = "사용자 휴대폰 번호", example = "010-1234-5678")
    private String phoneNumber;

    @Schema(description = "사용자 주소", example = "부산광역시 부산진구 시나브로 12 봉사아파트 802호")
    private String address;
}
