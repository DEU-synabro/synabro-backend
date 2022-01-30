package com.deu.synabro.http.response;

import com.deu.synabro.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Schema(description = "사용자 전체 회원 목록 응답")
public class MemberListResponse {
    @Schema(description = "조회 결과 개수", defaultValue = "0")
    private int count;

    @Schema(description = "조회 결과")
    private Member data;

    public MemberListResponse(Member data) {
        this.data = data;
        if(data instanceof List) {
            this.count = ((List<?>)data).size();
        } else {
            this.count = 1;
        }
    }
}
