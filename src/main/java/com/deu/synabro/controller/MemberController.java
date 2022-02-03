package com.deu.synabro.controller;

import com.deu.synabro.entity.Member;
import com.deu.synabro.http.response.ErrorResponse;
import com.deu.synabro.http.response.MemberListResponse;
import com.deu.synabro.service.MemberService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/members")
@RestController
public class MemberController {
    private MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @ApiOperation(value = "사용자 전체 정보 조회", notes = "사용자 전체 정보를 반환합니다.", tags = "member")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "페이지 번호", required = false, dataType = "integer", paramType = "query", defaultValue = "0"),
            @ApiImplicitParam(name = "size", value = "한 페이지당 불러올 콘텐츠 개수", required = false, dataType = "integer", paramType = "query", defaultValue = "10"),
            @ApiImplicitParam(name = "sort", value = "정렬방법(id,desc,asc)", required = false, dataType = "string", paramType = "query", defaultValue = "asc")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "전체 사용자 정보 조회 성공",
                         content = {@Content(schema = @Schema(implementation = PagedModel.class))}),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근")
    })
    @GetMapping(value = "")
    public @ResponseBody ResponseEntity<PagedModel<Member>> getMembers(@PageableDefault Pageable pageable) {
        Page<Member> members = memberService.findAll(pageable);
        PagedModel.PageMetadata pageMetadata =
                new PagedModel.PageMetadata(pageable.getPageSize(), members.getNumber(), members.getTotalElements());
        PagedModel<Member> resources = PagedModel.of(members.getContent(), pageMetadata);
        resources.add(linkTo(methodOn(MemberController.class).getMembers(pageable)).withSelfRel());
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }
}
