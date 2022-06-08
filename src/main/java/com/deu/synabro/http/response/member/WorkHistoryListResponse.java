package com.deu.synabro.http.response.member;

import com.deu.synabro.controller.MemberController;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
public class WorkHistoryListResponse {
    private final PagedModel<WorkHistoryResponse> work;

    public WorkHistoryListResponse(List<WorkHistoryResponse> work, Pageable pageable) {
        final int start = (int)pageable.getOffset();
        final int end = Math.min((start + 4), work.size());
        Page<WorkHistoryResponse> workPage = new PageImpl<>(work.subList(start, end), pageable, work.size());

        PagedModel.PageMetadata pageMetadata =
                new PagedModel.PageMetadata(pageable.getPageSize(), workPage.getNumber(), workPage.getTotalElements());
        this.work = PagedModel.of(workPage.getContent(), pageMetadata);
        this.work.add(linkTo(methodOn(MemberController.class).getMembers(pageable)).withSelfRel());
    }
}
