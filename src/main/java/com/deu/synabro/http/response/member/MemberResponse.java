package com.deu.synabro.http.response.member;

import com.deu.synabro.entity.Member;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MemberResponse extends WorkHistoryListResponse {
    private final String username;
    private final String introduction;
    private final String email;
    private final String phoneNumber;
    private final String address;
    private final Short volunteerTime;
    private final Short workNumber;
    private final Short warnNumber;

    public MemberResponse(Pageable pageable) {
        super(pageable);
        this.username = "하지민";
        this.introduction = "긍정적인 영향을 만들고 싶어요";
        this.email = "volunteer@synabro.com";
        this.phoneNumber = "010-1234-5678";
        this.address = "부산광역시 부산진구 시나브로 12 봉사아파트 802호";
        this.volunteerTime = 124;
        this.workNumber = 18;
        this.warnNumber = 0;
    }

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
