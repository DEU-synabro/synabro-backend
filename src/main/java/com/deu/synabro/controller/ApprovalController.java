package com.deu.synabro.controller;

import com.deu.synabro.entity.Work;
import com.deu.synabro.entity.enums.ApprovalType;
import com.deu.synabro.entity.enums.SearchOption;
import com.deu.synabro.http.request.WorkRequest;
import com.deu.synabro.http.response.GeneralResponse;
import com.deu.synabro.http.response.WorkListResponse;
import com.deu.synabro.http.response.WorkPageResponse;
import com.deu.synabro.http.response.WorkResponse;
import com.deu.synabro.service.WorkService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
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

    @PatchMapping("/permit/{work_id}")
    public ResponseEntity<GeneralResponse> permitWork(@Parameter(description = "고유 아이디") @PathVariable(name = "work_id") UUID uuid){
        try{
            workService.permitWork(workService.findByIdx(uuid));
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.OK, "봉사 요청글이 수정되었습니다"), HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.NOT_FOUND,"수정할 봉사 요청글이 없습니다."), HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/refuse/{work_id}")
    public ResponseEntity<GeneralResponse> refusetWork(@Parameter(description = "고유 아이디") @PathVariable(name = "work_id") UUID uuid){
        try{
            workService.refusetWork(workService.findByIdx(uuid));
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.OK, "봉사 요청글이 수정되었습니다"), HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.NOT_FOUND,"수정할 봉사 요청글이 없습니다."), HttpStatus.NOT_FOUND);
        }
    }
}
