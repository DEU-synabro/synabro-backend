package com.deu.synabro.http.response;

import com.deu.synabro.controller.MemberController;
import com.deu.synabro.entity.Member;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter @Setter
public class MemberListResponse {
    private PagedModel<Member> members;

//    public MemberListResponse(Pageable pageable, Page<Member> memberPage) {
//        PagedModel.PageMetadata pageMetadata =
//                new PagedModel.PageMetadata(pageable.getPageSize(), memberPage.getNumber(), memberPage.getTotalElements());
//        members = PagedModel.of(memberPage.getContent(), pageMetadata);
//        members.add(linkTo(methodOn(MemberController.class).getMembers(pageable)).withSelfRel());
//    }
}
