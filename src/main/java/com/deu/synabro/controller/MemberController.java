package com.deu.synabro.controller;

import com.deu.synabro.entity.Member;
import com.deu.synabro.service.MemberService;
import com.deu.synabro.http.response.MemberListResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/*
 * 이 클래스는 회원 정보 관리 및
 * 기본 CRUD, 로그인, 회원가입에 관한
 * 메소들이 정의된 클래스이다.
 *
 * @author Jiyoon Bak
 * @version 1.0
 */
@Tag(name = "member", description = "사용자 관리 API")
@RestController
@RequestMapping("/api/members")
public class MemberController {
    private MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @ApiOperation(value = "사용자 전체 정보 조회", notes = "사용자 전체 정보를 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "전체 사용자 정보 조회 성공",
                    content = @Content(schema = @Schema(implementation = MemberListResponse.class))),
    })
    @GetMapping(value = "/")
    public @ResponseBody ResponseEntity<?> getMembers(@PageableDefault Pageable pageable) {
        Page<Member> members = memberService.findAll(pageable);
        PagedModel.PageMetadata pageMetadata =
                new PagedModel.PageMetadata(pageable.getPageSize(), members.getNumber(), members.getTotalElements());
        PagedModel<Member> resources = PagedModel.of(members.getContent(), pageMetadata);
        resources.add(linkTo(methodOn(MemberController.class).getMembers(pageable)).withSelfRel());
        return ResponseEntity.ok(resources);
    }
}
