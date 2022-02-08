package com.deu.synabro.controller;

import com.deu.synabro.config.JwtProvider;
import com.deu.synabro.entity.Member;

import com.deu.synabro.repository.MemberRepository;
import com.deu.synabro.service.MemberService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.Collections;
import java.util.Map;
import java.util.UUID;

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
@RequiredArgsConstructor
@RestController
public class MemberController {
    @Autowired
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

//    @Autowired
//    public MemberController(MemberService memberService) { this.memberService = memberService; }

//    @ApiOperation(value = "사용자 전체 정보 조회", notes = "사용자 전체 정보를 반환합니다.", tags = "member")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "page", value = "페이지 번호", dataType = "integer", paramType = "query", defaultValue = "0"),
//            @ApiImplicitParam(name = "size", value = "한 페이지당 불러올 콘텐츠 개수", dataType = "integer", paramType = "query", defaultValue = "10"),
//            @ApiImplicitParam(name = "sort", value = "정렬방법(id,desc,asc)", dataType = "string", paramType = "query", defaultValue = "asc")
//    })
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "전체 사용자 정보 조회 성공",
//                         content = {@Content(schema = @Schema(implementation = MemberListResponse.class))}),
//            @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근")
//    })
//    @GetMapping(value = "")
//    public @ResponseBody ResponseEntity<MemberListResponse> getMembers(@PageableDefault Pageable pageable) {
//        Page<Member> members = memberService.findAll(pageable);
//        PagedModel.PageMetadata pageMetadata =
//                new PagedModel.PageMetadata(pageable.getPageSize(), members.getNumber(), members.getTotalElements());
//        PagedModel<Member> resources = PagedModel.of(members.getContent(), pageMetadata);
//        resources.add(linkTo(methodOn(MemberController.class).getMembers(pageable)).withSelfRel());
//        MemberListResponse memberListResponse = new MemberListResponse(pageable, members);
//        return new ResponseEntity<>(memberListResponse, HttpStatus.OK);
//    }
//
//    @Operation(summary = "사용자 등록", description = "사용자가 회원가입시 사용자를 등록합니다.", tags = "member",
//               responses = {
//                   @ApiResponse(responseCode = "200", description = "사용자 등록 성공",
//                                content = @Content(mediaType = "application/json",
//                                                   schema = @Schema(implementation = GeneralResponse.class),
//                                                   examples = @ExampleObject(value = SET_MEMBER_OK))),
//                   @ApiResponse(responseCode = "400", description = "필수 파라미터 누락",
//                                content = @Content(mediaType = "application/json",
//                                                   schema = @Schema(implementation = GeneralResponse.class),
//                                                   examples = @ExampleObject(value = SET_MEMBER_BAD_REQUEST)))
//               })
//    @PostMapping(value = "")
//    public ResponseEntity<GeneralResponse> setMember(@RequestBody SignUpRequest signUpRequest) {
//        if (signUpRequest.getEmail() == null || signUpRequest.getPassword() == null || signUpRequest.getUsername() == null) {
//            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.BAD_REQUEST, "잘못된 접근입니다. email, password, username을 확인해주세요."), HttpStatus.BAD_REQUEST);
//        }
//        Member member = new Member(signUpRequest.getEmail(), signUpRequest.getPassword(), signUpRequest.getUsername());
//        memberService.save(member);
//        return new ResponseEntity<>(GeneralResponse.of(HttpStatus.OK, "사용자 등록에 성공하였습니다."), HttpStatus.OK);
//    }
//
//
//    private static final String SET_MEMBER_OK = "{\n" +
//                                                "    \"code\" : 200\n" +
//                                                "    \"message\" : \"사용자 등록에 성공하였습니다.\"\n" +
//                                                "}";
//    private static final String SET_MEMBER_BAD_REQUEST = "{\n" +
//                                                         "    \"code\" : 400\n" +
//                                                         "    \"message\" : \"잘못된 접근입니다. email, password, username을 확인해주세요.\"\n" +
//                                                         "}";

    // 회원가입
    @PostMapping("/join")
    public UUID join(@RequestBody Map<String, String> user) {
        return memberRepository.save(Member.builder()
                .email(user.get("email"))
                .password(passwordEncoder.encode(user.get("password")))
                .username(user.get("username"))
                .roles(Collections.singletonList("ROLE_USER")) // 최초 가입시 USER 로 설정
                .build()).getIdx();
    }

    // 로그인
    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> user) {
        Member member = memberRepository.findByEmail(user.get("email"))
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));
        if (!passwordEncoder.matches(user.get("password"), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        return jwtProvider.createToken(member.getUsername(), member.getRoles());
    }
}
