package com.deu.synabro.http.response.member;

import com.deu.synabro.controller.MemberController;
import lombok.Getter;
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

@Getter
public class WorkHistoryListResponse {
    private final PagedModel<WorkHistoryResponse> work;

    public WorkHistoryListResponse(Pageable pageable) {
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
}
