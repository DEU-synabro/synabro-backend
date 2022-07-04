package com.deu.synabro.controller;

import com.deu.synabro.entity.Docs;
import com.deu.synabro.entity.Message;
import com.deu.synabro.entity.Work;
import com.deu.synabro.entity.enums.SearchOption;
import com.deu.synabro.entity.enums.StatusEnum;
import com.deu.synabro.http.request.WorkRequest;
import com.deu.synabro.http.response.*;
import com.deu.synabro.repository.DocsRepository;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Tag(name="Work", description = "봉사 요청 API")
@RestController
@RequestMapping("/api/works")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class WorkController {

    @Autowired
    DocsRepository docsRepository;

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

        if (file!=null) {
            if(file.getOriginalFilename().contains(".mp4")){
                videoService.saveVideo(file);
            }else{
                Docs docs = docsService.saveDocs(file);
                Work work = workService.setContent(workRequest, userId, docs);
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
        try{
            Work work = workService.findByIdx(uuid);
            return new ResponseEntity<>(workService.getContentsResponse(work), HttpStatus.OK);
        }catch ( NullPointerException e){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
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
        try{
            workService.deleteById(id);
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.OK,"봉사 요청글이 삭제되었습니다."), HttpStatus.OK);
        } catch (Exception e){
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
                                                            @Parameter @RequestBody WorkRequest workRequest){
        try{
            Work work = workService.findByIdx(id);
            workService.updateContents(workRequest, work);
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.OK, "봉사 요청글이 수정되었습니다"), HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.NOT_FOUND,"수정할 봉사 요청글이 없습니다."), HttpStatus.NOT_FOUND);
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
        Page<Work> works;
        String searchOption = option.getValue();
        List<WorkListResponse> workListResponseList = new ArrayList<>();
        WorkListResponse workListResponse;
        WorkPageResponse workPageResponse;

        if(keyword==null){
            works = workService.findAll(pageable);
            addWorkListResponse(works, workListResponseList);
            workPageResponse = new WorkPageResponse(pageable, works, option, null, workListResponseList);
        }else {
            if(searchOption=="제목+내용"){
                works = workService.findByTitleOrContents(pageable, keyword, keyword);
            }else {
                works = workService.findByTitle(pageable, keyword);
            }
            if (works.getContent().isEmpty()) {
                WorkListResponse.addNullWorkListResponse(workListResponseList);
            } else {
                addWorkListResponse(works, workListResponseList);
            }
            workPageResponse = new WorkPageResponse(pageable, works, option, keyword, workListResponseList);
        }
        return new ResponseEntity<>(workPageResponse, HttpStatus.OK);
    }

    private void addWorkListResponse(Page<Work> works, List<WorkListResponse> contentsResponseList){
        if(works.getSize()>=works.getTotalElements()){
            for(int i=0; i<works.getTotalElements(); i++){
                WorkListResponse workListResponse = WorkListResponse.builder()
                        .idx(works.getContent().get(i).getIdx())
                        .title(works.getContent().get(i).getTitle())
                        .createdDate(works.getContent().get(i).getCreatedDate())
                        .endedDate(works.getContent().get(i).getEndedDate())
                        .build();
                contentsResponseList.add(workListResponse);
            }
        }else {
            int contentSize = works.getSize()*works.getNumber();
            for(int i=0; i<works.getSize();i++){
                if(contentSize>=works.getTotalElements())
                    break;
                WorkListResponse workListResponse = WorkListResponse.builder()
                        .idx(works.getContent().get(i).getIdx())
                        .title(works.getContent().get(i).getTitle())
                        .createdDate(works.getContent().get(i).getCreatedDate())
                        .endedDate(works.getContent().get(i).getEndedDate())
                        .build();
                contentsResponseList.add(workListResponse);
                contentSize++;
            }
        }
    }

    private static Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    ResourceLoader resourceLoader;

    @CrossOrigin(origins = "*", exposedHeaders = {"Content-Disposition"}, maxAge = 3600)
    @GetMapping("/download/{work_id}")
    public ResponseEntity<Object> download(@Parameter(description = "고유 아이디")
                                               @PathVariable(name = "work_id") UUID uuid) throws IOException {
        return docsService.downDocs(uuid);
    }

}
