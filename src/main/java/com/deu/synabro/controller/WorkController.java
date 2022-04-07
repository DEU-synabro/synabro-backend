package com.deu.synabro.controller;

import com.deu.synabro.entity.Member;
import com.deu.synabro.entity.Work;
import com.deu.synabro.entity.enums.SearchOption;
import com.deu.synabro.http.request.WorkRequest;
import com.deu.synabro.http.request.WorkUpdateRequest;
import com.deu.synabro.http.response.*;
import com.deu.synabro.service.MemberService;
import com.deu.synabro.service.WorkService;
import com.deu.synabro.service.DocsService;
import com.deu.synabro.service.VideoService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Tag(name="Work", description = "봉사 요청 API")
@RestController
@RequestMapping("/api/works")
@AllArgsConstructor
public class WorkController {
    @Autowired
    WorkService workService;

    @Autowired
    DocsService docsService;

    @Autowired
    VideoService videoService;

    @Autowired
    MemberService memberService;

    private static final String DELETE_WORKS = "{\n" +
            "    \"code\" : 204\n" +
            "    \"message\" : \"봉사 요청글이 삭제되었습니다.\"\n" +
            "}";

    private static final String NOT_WORKS = "{\n" +
            "    \"code\" : 404\n" +
            "    \"message\" : \"봉사 요청글이 없습니다.\"\n" +
            "}";

    private static final String CREATE_WORKS = "{\n" +
            "    \"code\" : 200\n" +
            "    \"message\" : \"봉사 요청글이 생성되었습니다.\"\n" +
            "}";

    private static final String UPDATE_WORKS = "{\n" +
            "    \"code\" : 200\n" +
            "    \"message\" : \"봉사 요청글이 수정되었습니다.\"\n" +
            "}";
    @Operation(tags = "Work", summary = "봉사 요청글을 생성합니다.",
            responses={
                    @ApiResponse(responseCode = "200", description = "봉사 요청글 생성 성공",
                            content = @Content(schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(value = CREATE_WORKS))),
                    @ApiResponse(responseCode = "404", description = "삭제할 봉사 요청글이 없음",
                            content = @Content(schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(value = NOT_WORKS)))
            })
    @PostMapping("")
    public ResponseEntity<GeneralResponse> createWork(
            @Parameter(
                    description = "Files to be uploaded",
                    content = @Content(mediaType = MediaType.ALL_VALUE)  // Won't work without OCTET_STREAM as the mediaType.
            )
            @RequestPart(required = false) MultipartFile file,
            @Parameter(name = "contentsRequest", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
            @RequestPart(name = "contentsRequest") WorkRequest workRequest) throws IOException {
        UUID userId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
        workRequest.setUserId(userId);
        Work work = workService.setContent(workRequest);
        if (file!=null) {
            if(file.getOriginalFilename().contains(".mp4")){
                videoService.saveVideo(file, work);
            }else{
                docsService.saveDocs(file, work);
            }
        }
        return new ResponseEntity<>(GeneralResponse.of(HttpStatus.OK,"봉사 요청글이 생성되었습니다."), HttpStatus.OK);
    }

    @Operation(tags = "Work", summary = "id 값으로 봉사 요청글을 찾습니다.",
            responses={
                    @ApiResponse(responseCode = "200", description = "id 값으로 봉사 요청글 정보 조회 성공",
                            content = @Content(schema = @Schema(implementation = WorkResponse.class)))
            })
    @GetMapping("/{work_id}")
    public ResponseEntity<WorkResponse> getContents(@Parameter(description = "고유 아이디")
                                                              @PathVariable(name = "work_id") UUID uuid){
        WorkResponse workResponse;
        try{
            workResponse = workService.findByIdAndGetResponse(uuid);
            return new ResponseEntity<>(workResponse,HttpStatus.OK);
        }catch (IllegalArgumentException e){
            workResponse = workService.getNullResponse();
            return new ResponseEntity<>(workResponse,HttpStatus.NOT_FOUND);
        }
    }

    @Operation(tags = "Work", summary = "봉사 요청글을 삭제 합니다.",
            responses={
                    @ApiResponse(responseCode = "204", description = "봉사 요청글 삭제 성공",
                            content = @Content(schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(value = DELETE_WORKS))),
                    @ApiResponse(responseCode = "404", description = "삭제할 봉사 요청글이 없음",
                            content = @Content(schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(value = NOT_WORKS)))
            })
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping("/{work_id}")
    public ResponseEntity<GeneralResponse> deleteContents(@Parameter(description = "고유 아이디")
                                                               @PathVariable(name = "work_id") UUID id){
        if(workService.deleteById(id)){
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.OK,"봉사 요청글이 삭제되었습니다."), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.NOT_FOUND,"삭제할 봉사 요청글이 없습니다."), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(tags = "Work", summary = "봉사 요청글을 수정합니다.",
            responses={
                    @ApiResponse(responseCode = "200", description = "봉사 요청글 수정 성공",
                            content = @Content(schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(value = UPDATE_WORKS))),
                    @ApiResponse(responseCode = "404", description = "삭제할 봉사 요청 글이 없음",
                            content = @Content(schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(value = NOT_WORKS)))
            })
    @PatchMapping("/{work_id}")
    public ResponseEntity<GeneralResponse> updateVolunteerWork(@Parameter(description = "고유 아이디") @PathVariable(name = "work_id") UUID id,
                                                            @Parameter @RequestBody WorkUpdateRequest workUpdateRequest){
        Work work = workService.findById(id);
        if(work==null){
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.NOT_FOUND,"수정할 봉사 요청글이 없습니다."), HttpStatus.NOT_FOUND);
        }else{
            workService.updateContents(workUpdateRequest, work);
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.OK, "봉사 요청글이 수정되었습니다"), HttpStatus.OK);
        }
    }

    @Operation(tags = "Work", summary = "제목, 제목+내용으로 봉사 요청글을 찾습니다.",
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
    @GetMapping("")
    public ResponseEntity<WorkPageResponse> getPagingContents(@PageableDefault Pageable pageable,
                                                              @RequestParam(name="searchOption", required = false, defaultValue = "title") SearchOption option,
                                                              @RequestParam(name="keyword", required = false) String keyword){
        Page<Work> contents;
        String searchOption = option.getValue();
        List<WorkListResponse> contentsResponseList = new ArrayList<>();
        WorkListResponse workListResponse;
        WorkPageResponse workPageResponse;

        if(keyword==null){
            contents = workService.findAll(pageable);
            if(contents.getSize()>=contents.getTotalElements()){
                for(int i=0; i<contents.getTotalElements(); i++){
                    workListResponse = WorkListResponse.builder()
                            .idx(contents.getContent().get(i).getIdx())
                            .title(contents.getContent().get(i).getTitle())
                            .createdDate(contents.getContent().get(i).getCreatedDate())
                            .endedDate(contents.getContent().get(i).getEndedDate())
                            .build();
                    contentsResponseList.add(workListResponse);
                }
            }else {
                int contentSize = contents.getSize()*contents.getNumber();
                for(int i=0; i<contents.getSize();i++){
                    if(contentSize>=contents.getTotalElements())
                        break;
                    workListResponse = WorkListResponse.builder()
                            .idx(contents.getContent().get(i).getIdx())
                            .title(contents.getContent().get(i).getTitle())
                            .createdDate(contents.getContent().get(i).getCreatedDate())
                            .endedDate(contents.getContent().get(i).getEndedDate())
                            .build();
                    contentsResponseList.add(workListResponse);
                    contentSize++;
                }
            }
            workPageResponse = new WorkPageResponse(pageable, contents, option, null, contentsResponseList);
            return new ResponseEntity<>(workPageResponse, HttpStatus.OK);
        }else {
            if(searchOption=="제목+내용"){
                contents = workService.findByTitleOrContents(pageable, keyword, keyword);
            }else {
                contents = workService.findByTitle(pageable, keyword);
            }
            if (contents.getContent().isEmpty()) {
                workListResponse = WorkListResponse.builder()
                        .idx(null)
                        .title(null)
                        .createdDate(null)
                        .endedDate(null)
                        .build();
                contentsResponseList.add(workListResponse);
            } else {
                if(contents.getSize()>=contents.getTotalElements()){
                    for(int i=0; i<contents.getTotalElements(); i++){
                        workListResponse = WorkListResponse.builder()
                                .idx(contents.getContent().get(i).getIdx())
                                .title(contents.getContent().get(i).getTitle())
                                .createdDate(contents.getContent().get(i).getCreatedDate())
                                .endedDate(contents.getContent().get(i).getEndedDate())
                                .build();
                        contentsResponseList.add(workListResponse);
                    }
                }else {
                    int contentSize = contents.getSize()*contents.getNumber();
                    for(int i=0; i<contents.getSize();i++){
                        if(contentSize>=contents.getTotalElements())
                            break;
                        workListResponse = WorkListResponse.builder()
                                .idx(contents.getContent().get(i).getIdx())
                                .title(contents.getContent().get(i).getTitle())
                                .createdDate(contents.getContent().get(i).getCreatedDate())
                                .endedDate(contents.getContent().get(i).getEndedDate())
                                .build();
                        contentsResponseList.add(workListResponse);
                        contentSize++;
                    }
                }
            }
            workPageResponse = new WorkPageResponse(pageable, contents, option, keyword, contentsResponseList);
            return new ResponseEntity<>(workPageResponse, HttpStatus.OK);
        }
    }
}
