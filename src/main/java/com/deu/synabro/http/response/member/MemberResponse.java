package com.deu.synabro.http.response.member;

import com.deu.synabro.entity.Member;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MemberResponse extends WorkHistoryListResponse {
    @Schema(description = "사용자 이름", example = "하지민")
    private final String username;
    @Schema(description = "사용자 자기소개 글", example = "긍정적인 영향을 만들고 싶어요")
    private final String introduction;
    @Schema(description = "사용자 이메일", example = "volunteer@synabro.com")
    private final String email;
    @Schema(description = "사용자 휴대폰 번호", example = "010-1234-5678")
    private final String phoneNumber;
    @Schema(description = "사용자 주소", example = "부산광역시 부산진구 시나브로 12 봉사아파트 802호")
    private final String address;
    @Schema(description = "사용자 봉사 시간", example = "124")
    private final Short volunteerTime;
    @Schema(description = "사용자 작업물 개수", example = "18")
    private final Short workNumber;
    @Schema(description = "사용자 경고 누적 횟수", example = "0")
    private final Short warnNumber;

    public MemberResponse(Member member, Pageable pageable) {
        super(pageable);
        this.username = member.getUsername();
        this.introduction = member.getIntroduction();
        this.email = member.getEmail();
        this.phoneNumber = member.getPhone();
        this.address = member.getAddress();
        this.volunteerTime = member.getVolunteerTime();
        this.workNumber = member.getWorkNumber();
        this.warnNumber = member.getWarning();
    }
}
