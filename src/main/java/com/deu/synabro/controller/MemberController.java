package com.deu.synabro.controller;

import com.deu.synabro.entity.Authority;
import com.deu.synabro.http.request.member.MemberPatchRequest;
import com.deu.synabro.http.request.member.SignUpRequest;
import com.deu.synabro.http.response.GeneralResponse;
import com.deu.synabro.http.response.member.*;
import com.deu.synabro.service.MemberService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

/*
 * 이 클래스는 회원 정보 관리 및
 * 기본 CRUD, 로그인, 회원가입에 관한
 * 메소들이 정의된 클래스이다.
 *
 * @author Jiyoon Bak
 * @version 1.0
 */
@Tag(name = "Member", description = "사용자 관리 API")
@RequestMapping("/api/members")
@RestController
public class MemberController extends ResponseExample {
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) { this.memberService = memberService; }

    @Operation(summary = "사용자 정보 조회", description = "사용자 정보를 반환합니다.", tags = "Member",
               responses = {
                       @ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공",
                               content = {@Content(schema = @Schema(implementation = MemberResponse.class))}),
                       @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근")
               })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "페이지 번호", dataType = "integer", paramType = "query", defaultValue = "0"),
            @ApiImplicitParam(name = "size", value = "한 페이지당 불러올 콘텐츠 개수", dataType = "integer", paramType = "query", defaultValue = "10"),
            @ApiImplicitParam(name = "sort", value = "정렬방법(id,desc,asc)", dataType = "string", paramType = "query", defaultValue = "asc")
    })
    @GetMapping(value = "")
    public @ResponseBody ResponseEntity<MemberResponse> getMembers(@PageableDefault(size = 4) Pageable pageable) {
        return new ResponseEntity<>(memberService.findMember(pageable), HttpStatus.OK);
    }

    @Operation(summary = "사용자 작업 목록 조회", description = "사용자 작업 목록을 반환합니다.", tags = "Member",
            responses = {
                    @ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공",
                            content = {@Content(schema = @Schema(implementation = WorkHistoryListResponse.class))}),
                    @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근")
            })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "페이지 번호", dataType = "integer", paramType = "query", defaultValue = "0"),
            @ApiImplicitParam(name = "size", value = "한 페이지당 불러올 콘텐츠 개수", dataType = "integer", paramType = "query", defaultValue = "10"),
            @ApiImplicitParam(name = "sort", value = "정렬방법(id,desc,asc)", dataType = "string", paramType = "query", defaultValue = "asc")
    })
    @GetMapping(value = "/list")
    public @ResponseBody ResponseEntity<WorkHistoryListResponse> getMemberWorkLists(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(memberService.findWorkList(pageable));
    }

    @Operation(summary = "사용자 작업 목록 조회", description = "사용자 작업 목록을 반환합니다.", tags = "Member",
            responses = {
                    @ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공",
                            content = {@Content(schema = @Schema(implementation = WorkHistoryDetailResponse.class))}),
                    @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근")
            })
    @GetMapping(value = "/list/{id}")
    public @ResponseBody ResponseEntity<WorkHistoryDetailResponse> getMemberWorkDetail(
            @PathVariable(name = "id") UUID id,
            @RequestParam String type
    ) {
        if (type.equals("VOLUNTEER")) {
            return ResponseEntity.ok(memberService.findVolunteerWork(id));
        } else if(type.equals("INSPECTION")) {
            return ResponseEntity.ok(memberService.findInspectionWork(id));
        }
        return new ResponseEntity<>(new WorkHistoryDetailResponse(), HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "사용자 등록", description = "사용자가 회원가입시 사용자를 등록합니다.", tags = "Member",
               responses = {
                   @ApiResponse(responseCode = "200", description = "사용자 등록 성공",
                                content = @Content(mediaType = "application/json",
                                                   schema = @Schema(implementation = GeneralResponse.class),
                                                   examples

                                                           = @ExampleObject(value = SET_MEMBER_OK))),
                   @ApiResponse(responseCode = "400", description = "필수 파라미터 누락",
                                content = @Content(mediaType = "application/json",
                                                   schema = @Schema(implementation = GeneralResponse.class),
                                                   examples = @ExampleObject(value = SET_MEMBER_BAD_REQUEST)))
               })
    @PostMapping(value = "/signup")
    public ResponseEntity<GeneralResponse> setMember(@RequestBody SignUpRequest signUpRequest) {
        if (memberService.checkSignUpRequest(signUpRequest)) {
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.BAD_REQUEST, "잘못된 접근입니다. email, password, username을 확인해주세요."), HttpStatus.BAD_REQUEST);
        }

        memberService.signUp(signUpRequest, Authority.builder().authorityName("ROLE_USER").build());

        return new ResponseEntity<>(GeneralResponse.of(HttpStatus.OK, "사용자 등록에 성공하였습니다."), HttpStatus.OK);
    }

    @Operation(summary = "사용자 정보 수정", description = "사용자의 개인 정보를 수정합니다..", tags = "Member",
            responses = {
                    @ApiResponse(responseCode = "200", description = "사용자 정보 수정 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = GeneralResponse.class),
                                    examples

                                            = @ExampleObject(value = SET_UPDATE_OK))),
                    @ApiResponse(responseCode = "400", description = "필수 파라미터 누락",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(value = SET_BAD_REQUEST)))
            })
    @PatchMapping(value = "/update")
    public ResponseEntity<GeneralResponse> updateMember(@RequestBody MemberPatchRequest request) {
        memberService.update(request);
        return new ResponseEntity<>(GeneralResponse.of(HttpStatus.OK, "사용자 정보 수정에 성공하였습니다."), HttpStatus.OK);
    }

    @Operation(summary = "수혜자 등록", description = "사용자가 회원가입시 사용자를 등록합니다.", tags = "Beneficiary",
            responses = {
                    @ApiResponse(responseCode = "200", description = "사용자 등록 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = GeneralResponse.class),
                                    examples

                                            = @ExampleObject(value = SET_MEMBER_OK))),
                    @ApiResponse(responseCode = "400", description = "필수 파라미터 누락",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(value = SET_MEMBER_BAD_REQUEST)))
            })
    @PostMapping(value = "/beneficiary/signup")
    public ResponseEntity<GeneralResponse> setBeneficiary(@RequestBody SignUpRequest signUpRequest) {
        if (memberService.checkSignUpRequest(signUpRequest)) {
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.BAD_REQUEST, "잘못된 접근입니다. email, password, username을 확인해주세요."), HttpStatus.BAD_REQUEST);
        }

        memberService.signUp(signUpRequest, Authority.builder().authorityName("ROLE_BENEFICIARY").build());

        return new ResponseEntity<>(GeneralResponse.of(HttpStatus.OK, "사용자 등록에 성공하였습니다."), HttpStatus.OK);
    }

}
