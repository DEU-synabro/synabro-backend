package com.deu.synabro.controller;

import com.deu.synabro.entity.VolunteerWork;
import com.deu.synabro.entity.Work;
import com.deu.synabro.entity.enums.ApprovalType;
import com.deu.synabro.entity.enums.SearchOption;
import com.deu.synabro.http.request.WorkRequest;
import com.deu.synabro.http.response.*;
import com.deu.synabro.service.VolunteerWorkService;
import com.deu.synabro.service.WorkService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Tag(name = "Approval", description = "승인 API")
@RestController
@RequestMapping("/api/approval")
public class ApprovalController {

    @Autowired
    WorkService workService;

    @Autowired
    VolunteerWorkService volunteerWorkService;

    private static final String PERMIT_WORK = "{\n" +
            "    \"code\" : 204\n" +
            "    \"message\" : \"봉사 요청글 신청이 승인되었습니다.\"\n" +
            "}";
    private static final String REFUSE_WORK = "{\n" +
            "    \"code\" : 204\n" +
            "    \"message\" : \"봉사 요청글 신청이 취소되었습니다.\"\n" +
            "}";
    private static final String NOT_WORK = "{\n" +
            "    \"code\" : 404\n" +
            "    \"message\" : \"봉사 요청글이 없습니다.\"\n" +
            "}";
    private static final String PERMIT_VOLUNTEER_WORK = "{\n" +
            "    \"code\" : 204\n" +
            "    \"message\" : \"봉사 수행글 신청이 승인되었습니다.\"\n" +
            "}";
    private static final String REFUSE_VOLUNTEER_WORK = "{\n" +
            "    \"code\" : 204\n" +
            "    \"message\" : \"봉사 수행글 신청이 취소되었습니다.\"\n" +
            "}";
    private static final String NOT_VOLUNTEER_WORK = "{\n" +
            "    \"code\" : 404\n" +
            "    \"message\" : \"봉사 수행글이 없습니다.\"\n" +
            "}";

    @Operation(tags = "Approval", summary = "제목, 제목+내용으로 봉사 요청글을 찾습니다.",
            responses={
                    @ApiResponse(responseCode = "200", description = "제목, 제목+내용으로 글 정보 조회 성공",
                            content = @Content(schema = @Schema(implementation = WorkPageResponse.class)))
            })
    @io.swagger.annotations.ApiResponses(
            @io.swagger.annotations.ApiResponse(
                    response = WorkPageResponse.class, message = "ok", code=200)
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "페이지 번호", dataType = "integer", paramType = "query", defaultValue = "0"),
            @ApiImplicitParam(name = "size", value = "한 페이지당 불러올 콘텐츠 개수", dataType = "integer", paramType = "query", defaultValue = "10"),
            @ApiImplicitParam(name = "sort", value = "정렬방법(id,desc,asc)", dataType = "string", paramType = "query", defaultValue = "asc")
    })
    @GetMapping("/work")
    public ResponseEntity<WorkPageResponse> getPagingWorkApproval(@PageableDefault Pageable pageable,
                                                                  @RequestParam(name="searchOption", required = false, defaultValue = "title") SearchOption option,
                                                                  @RequestParam(name="keyword", required = false) String keyword){
        Page<Work> workPage;
        String searchOption = option.getValue();
        List<WorkListResponse> workListResponseList = new ArrayList<>();
        WorkPageResponse workPageResponse;

        if(keyword==null){
            workPage = workService.findAll(pageable, ApprovalType.wait);
            workService.addWorkListResponse(workPage, workListResponseList);
            workPageResponse = new WorkPageResponse(pageable, workPage, option, null, workListResponseList);
        }else {
            if(searchOption=="제목+내용"){
                workPage = workService.findByTitleOrContents(pageable, keyword, keyword, ApprovalType.wait);
            }else {
                workPage = workService.findByTitle(pageable, keyword, ApprovalType.wait);
            }
            if (workPage.getContent().isEmpty()) {
                WorkListResponse.addNullWorkListResponse(workListResponseList);
            } else {
                workService.addWorkListResponse(workPage, workListResponseList);
            }
            workPageResponse = new WorkPageResponse(pageable, workPage, option, keyword, workListResponseList);
        }
        return new ResponseEntity<>(workPageResponse, HttpStatus.OK);
    }

    @Operation(tags = "Approval", summary = "봉사 요청글 신청을 승인합니다.",
            responses={
                    @ApiResponse(responseCode = "200", description = "봉사 수행글 신청 승인 성공",
                            content = @Content(schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(value = PERMIT_WORK))),
                    @ApiResponse(responseCode = "404", description = "봉사 수행글이 없음",
                            content = @Content(schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(value = NOT_WORK)))
            })
    @PatchMapping("/work/permit/{work_id}")
    public ResponseEntity<GeneralResponse> permitWork(@Parameter(description = "고유 아이디") @PathVariable(name = "work_id") UUID uuid){
        try{
            workService.permitWork(workService.findByIdx(uuid));
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.OK, "봉사 요청글 신청이 승인되었습니다"), HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.NOT_FOUND,"봉사 요청글이 없습니다."), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(tags = "Approval", summary = "봉사 요청글 신청을 거절합니다.",
            responses={
                    @ApiResponse(responseCode = "200", description = "봉사 수행글 신청 거절 성공",
                            content = @Content(schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(value = REFUSE_WORK))),
                    @ApiResponse(responseCode = "404", description = "봉사 수행글이 없음",
                            content = @Content(schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(value = NOT_WORK)))
            })
    @PatchMapping("/work/refuse/{work_id}")
    public ResponseEntity<GeneralResponse> refusetWork(@Parameter(description = "고유 아이디") @PathVariable(name = "work_id") UUID uuid){
        try{
            workService.refuseWork(workService.findByIdx(uuid));
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.OK, "봉사 요청글 신청이이 거절되었습니다"), HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.NOT_FOUND,"봉사 요청글이 없습니다."), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(tags = "Approval", summary = "제목, 제목+내용으로 봉사 수행글을 찾습니다.",
            responses={
                    @ApiResponse(responseCode = "200", description = "제목, 제목+내용으로 글 정보 조회 성공",
                            content = @Content(schema = @Schema(implementation = VolunteerWorkPageResponse.class)))
            })
    @io.swagger.annotations.ApiResponses(
            @io.swagger.annotations.ApiResponse(
                    response = VolunteerWorkPageResponse.class, message = "ok", code=200)
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "페이지 번호", dataType = "integer", paramType = "query", defaultValue = "0"),
            @ApiImplicitParam(name = "size", value = "한 페이지당 불러올 콘텐츠 개수", dataType = "integer", paramType = "query", defaultValue = "10"),
            @ApiImplicitParam(name = "sort", value = "정렬방법(id,desc,asc)", dataType = "string", paramType = "query", defaultValue = "asc")
    })
    @GetMapping("/volunteerWorks")
    public ResponseEntity<VolunteerWorkPageResponse> getPagingVolunteerApproval(@PageableDefault Pageable pageable,
                                                                  @RequestParam(name="searchOption", required = false, defaultValue = "title") SearchOption option,
                                                                  @RequestParam(name="keyword", required = false) String keyword){
        Page<VolunteerWork> volunteerWorkPage;
        String searchOption = option.getValue();
        List<VolunteerListResponse> volunteerListResponseList = new ArrayList<>();
        VolunteerWorkPageResponse volunteerWorkPageResponse;

        if(keyword==null){
            volunteerWorkPage = volunteerWorkService.findAllApproval(pageable, ApprovalType.wait);
            volunteerWorkService.addVolunteerListResponse(volunteerWorkPage, volunteerListResponseList);
            volunteerWorkPageResponse = new VolunteerWorkPageResponse(pageable, volunteerWorkPage, option, null, volunteerListResponseList);
        }else {
            if(searchOption=="제목+내용"){
                volunteerWorkPage = volunteerWorkService.findByTitleOrContentsAndApproval(pageable, keyword, keyword, ApprovalType.wait);
            }else {
                volunteerWorkPage = volunteerWorkService.findByTitleAndApproval(pageable, keyword, ApprovalType.wait);
            }
            if (volunteerWorkPage.getContent().isEmpty()) {
                VolunteerListResponse.addNullVolunteerListResponse(volunteerListResponseList);
            } else {
                volunteerWorkService.addVolunteerListResponse(volunteerWorkPage, volunteerListResponseList);
            }
            volunteerWorkPageResponse = new VolunteerWorkPageResponse(pageable, volunteerWorkPage, option, keyword, volunteerListResponseList);
        }
        return new ResponseEntity<>(volunteerWorkPageResponse, HttpStatus.OK);
    }

    @Operation(tags = "Approval", summary = "봉사 수행글 신청을 승인합니다.",
            responses={
                    @ApiResponse(responseCode = "200", description = "봉사 수행글 신청 승인 성공",
                            content = @Content(schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(value = PERMIT_VOLUNTEER_WORK))),
                    @ApiResponse(responseCode = "404", description = "봉사 수행글이 없음",
                            content = @Content(schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(value = NOT_VOLUNTEER_WORK)))
            })
    @PatchMapping("/volunteerWorks/permit/{volunteer_work_id}")
    public ResponseEntity<GeneralResponse> permitVolunteer(@Parameter(description = "고유 아이디") @PathVariable(name = "volunteer_work_id") UUID uuid){
        try{
            volunteerWorkService.permitVolunteerWork(volunteerWorkService.findByIdx(uuid));
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.OK, "봉사 수행글 신청이 승인되었습니다."), HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.NOT_FOUND,"봉사 수행글이 없습니다."), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(tags = "Approval", summary = "봉사 수행글 신청을 거절합니다.",
            responses={
                    @ApiResponse(responseCode = "200", description = "봉사 수행글 신청 거절 성공",
                            content = @Content(schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(value = REFUSE_VOLUNTEER_WORK))),
                    @ApiResponse(responseCode = "404", description = "봉사 수행글이 없음",
                            content = @Content(schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(value = NOT_VOLUNTEER_WORK)))
            })
    @PatchMapping("/volunteerWorks/refuse/{volunteer_work_id}")
    public ResponseEntity<GeneralResponse> refuseVolunteer(@Parameter(description = "고유 아이디") @PathVariable(name = "volunteer_work_id") UUID uuid){
        try{
            volunteerWorkService.refuseVolunteerWork(volunteerWorkService.findByIdx(uuid));
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.OK, "봉사 수행글 신청이 거절되었습니다."), HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.NOT_FOUND,"봉사 수행글이 없습니다."), HttpStatus.NOT_FOUND);
        }
    }
}
