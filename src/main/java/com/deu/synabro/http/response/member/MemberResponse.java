package com.deu.synabro.http.response.member;

import com.deu.synabro.controller.MemberController;
import com.deu.synabro.entity.Member;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter @Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MemberResponse {
    private String username;
    private String introduction;
    private String email;
    private String phoneNumber;
    private String address;
    private Short volunteerTime;
    private Short workNumber;
    private Short warnNumber;
    private PagedModel<WorkHistoryResponse> work;

    public MemberResponse(Pageable pageable) {
        this.username = "하지민";
        this.introduction = "긍정적인 영향을 만들고 싶어요";
        this.email = "volunteer@synabro.com";
        this.phoneNumber = "010-1234-5678";
        this.address = "부산광역시 부산진구 시나브로 12 봉사아파트 802호";
        this.volunteerTime = 124;
        this.workNumber = 18;
        this.warnNumber = 0;

        List<WorkHistoryResponse> work = new ArrayList();
        work.add(WorkHistoryResponse.builder().type("VOLUNTEER").id(UUID.randomUUID()).title("[도서] 달러구트 꿈 백화점 전자화 요청").date(LocalDate.now()).build());
        work.add(WorkHistoryResponse.builder().type("INSPECTION").id(UUID.randomUUID()).title("동영상 자막 작업 부탁드립니다.").date(LocalDate.now()).build());
        work.add(WorkHistoryResponse.builder().type("VOLUNTEER").id(UUID.randomUUID()).title("사진에 찍힌 문서를 PDF 파일로 제작해주세요.").date(LocalDate.now()).build());
        work.add(WorkHistoryResponse.builder().type("INSPECTION").id(UUID.randomUUID()).title("사진에 찍힌 문서를 PDF 파일로 제작해주세요.").date(LocalDate.now()).build());
        work.add(WorkHistoryResponse.builder().type("VOLUNTEER").id(UUID.randomUUID()).title("[영상] 동영상 자막 작업 요청").date(LocalDate.now()).build());
        final int start = (int)pageable.getOffset();
        final int end = Math.min((start + 4), work.size());
        Page<WorkHistoryResponse> workPage = new PageImpl<>(work.subList(start, end), pageable, work.size());

        PagedModel.PageMetadata pageMetadata =
                new PagedModel.PageMetadata(pageable.getPageSize(), workPage.getNumber(), workPage.getTotalElements());
        this.work = PagedModel.of(workPage.getContent(), pageMetadata);
        this.work.add(linkTo(methodOn(MemberController.class).getMembers(pageable)).withSelfRel());

    }

    public MemberResponse(Member member, Pageable pageable) {
        this.username = member.getUsername();
        this.introduction = member.getIntroduction();
        this.email = member.getEmail();
        this.phoneNumber = member.getPhone();
        this.address = member.getAddress();
        this.volunteerTime = member.getVolunteerTime();
        this.workNumber = member.getWorkNumber();
        this.warnNumber = member.getWarning();

//        PagedModel.PageMetadata pageMetadata =
//                new PagedModel.PageMetadata(pageable.getPageSize(), memberPage.getNumber(), memberPage.getTotalElements());
//        members = PagedModel.of(memberPage.getContent(), pageMetadata);
//        members.add(linkTo(methodOn(MemberController.class).getMembers(pageable)).withSelfRel());
    }
}
