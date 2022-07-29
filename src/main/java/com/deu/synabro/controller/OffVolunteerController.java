package com.deu.synabro.controller;


import com.deu.synabro.entity.OffVolunteer;
import com.deu.synabro.entity.enums.SearchOption;
import com.deu.synabro.http.request.OffVolunteerUpdateRequest;
import com.deu.synabro.http.request.offVolunteer.OffVolunteerRequest;
import com.deu.synabro.http.response.*;
import com.deu.synabro.service.OffVolunteerApplicationService;
import com.deu.synabro.service.OffVolunteerService;
import com.deu.synabro.util.FileUtil;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 오프라인 봉사에 대한 CRUD 메소도들이 정의된 클래스입니다.
 *
 * @author tkfdkskarl56
 * @since 1.0
 */
@Tag(name="offVolunteer", description = "오프라인 봉사 모집 API")
@RestController
@RequestMapping("/api/offVolunteer")
@AllArgsConstructor
public class OffVolunteerController {
    @Autowired
    OffVolunteerService offVolunteerService;

    @Autowired
    FileUtil fileUtil;

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
    private static final String FORBIDDEN_WORKS = "{\n" +
            "    \"code\" : 403\n" +
            "    \"message\" : \"봉사 수혜자만 신청할 수 있습니다.\"\n" +
            "}";

    @Operation(tags = "offVolunteer", summary = "오프라인 봉사 모집글을 생성합니다.",
            responses={
                    @ApiResponse(responseCode = "200", description = "오프라인 봉사 모집글 생성 성공",
                            content = @Content(schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(value = CREATE_WORKS))),
                    @ApiResponse(responseCode = "403", description = "권한이 없습니다.",
                            content = @Content(schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(value = FORBIDDEN_WORKS)))
            })
    @PostMapping("")
    public ResponseEntity<GeneralResponse> createOffVolunteer(
        @Parameter(
            description = "Files to be uploaded",
            content = @Content(mediaType = MediaType.ALL_VALUE)  // Won't work without OCTET_STREAM as the mediaType.
        )
        @RequestPart(required = false) List<MultipartFile> files,
        @Parameter(name = "contentsRequest", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
        @RequestPart(name = "contentsRequest") OffVolunteerRequest offVolunteerRequest) {
        if (files!=null) {
            for(MultipartFile file : files){
                if(file.getOriginalFilename().contains(".mp4") || file.getOriginalFilename().contains(".avi")){
                    offVolunteerService.setOffVolunteerVideo(offVolunteerRequest, fileUtil.saveVideo(file));
                }
                if(file.getOriginalFilename().contains(".txt") || file.getOriginalFilename().contains(".png") || file.getOriginalFilename().contains(".jpg")){
                    offVolunteerService.setOffVolunteerDocs(offVolunteerRequest, fileUtil.saveDocs(file));
                }
            }
        }else {
            offVolunteerService.setOffVolunteer(offVolunteerRequest);
        }
        return new ResponseEntity<>(GeneralResponse.of(HttpStatus.OK,"오프라인 봉사 모집글이 생성되었습니다."), HttpStatus.OK);
    }

    @Operation(tags = "offVolunteer", summary = "id 값으로 오프라인 봉사 모집글을 찾습니다.",
            responses={
                    @ApiResponse(responseCode = "200", description = "id 값으로 오프라인 봉사 모집글 정보 조회 성공",
                            content = @Content(schema = @Schema(implementation = OffVolunteerResponse.class)))
            })
    @GetMapping("/{off_volunteer_id}")
    public ResponseEntity<OffVolunteerResponse> getOffVolunteer(@Parameter(description = "고유 아이디") @PathVariable(name = "off_volunteer_id") UUID uuid){
        try{
            OffVolunteer offVolunteer = offVolunteerService.findByIdx(uuid);
            return new ResponseEntity<>(offVolunteerService.getOffVolunteerResponse(offVolunteer), HttpStatus.OK);
        }catch(NullPointerException e){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(tags = "offVolunteer", summary = "오프라인 봉사 모집글을 삭제 합니다.",
            responses={
                    @ApiResponse(responseCode = "204", description = "오프라인 봉사 모집글 삭제 성공",
                            content = @Content(schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(value = DELETE_WORKS))),
                    @ApiResponse(responseCode = "404", description = "삭제할 오프라인 봉사 모집글이 없음",
                            content = @Content(schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(value = NOT_WORKS)))
            })
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping("/{off_volunteer_id}")
    public ResponseEntity<GeneralResponse> deleteOffVolunteer(@Parameter(description = "고유 아이디") @PathVariable(name = "off_volunteer_id") UUID uuid){
        try{
            offVolunteerService.deleteById(uuid);
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.OK,"봉사 요청글이 삭제되었습니다."), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.NOT_FOUND,"삭제할 봉사 요청글이 없습니다."), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(tags = "offVolunteer", summary = "오프라인 봉사 모집글을 수정합니다.",
            responses={
                    @ApiResponse(responseCode = "200", description = "오프라인 봉사 모집글 수정 성공",
                            content = @Content(schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(value = UPDATE_WORKS))),
                    @ApiResponse(responseCode = "404", description = "삭제할 오프라인 봉사 모집 글이 없음",
                            content = @Content(schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(value = NOT_WORKS)))
            })
    @PatchMapping("/{off_volunteer_id}")
    public ResponseEntity<GeneralResponse> updateOffVolunteer(@Parameter(description = "고유 아이디") @PathVariable(name = "off_volunteer_id") UUID uuid,
                                                              @Parameter @RequestBody OffVolunteerUpdateRequest offVolunteerUpdateRequest){
        try{
            OffVolunteer offVolunteer = offVolunteerService.findByIdx(uuid);
            offVolunteerService.updateOffVolunteer(offVolunteerUpdateRequest, offVolunteer);
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.OK, "봉사 모집글이 수정되었습니다"), HttpStatus.OK);
        }catch(IllegalArgumentException e){
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.NOT_FOUND,"수정할 봉사 모집글이 없습니다."), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(tags = "offVolunteer", summary = "제목, 제목+내용으로 봉사 요청글을 찾습니다.",
            responses={
                    @ApiResponse(responseCode = "200", description = "제목, 제목+내용으로 글 정보 조회 성공",
                            content = @Content(schema = @Schema(implementation = OffVolunteerPageResponse.class)))
            })
    @io.swagger.annotations.ApiResponses(
            @io.swagger.annotations.ApiResponse(
                    response = OffVolunteerPageResponse.class, message = "ok", code=200)
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "페이지 번호", dataType = "integer", paramType = "query", defaultValue = "0"),
            @ApiImplicitParam(name = "size", value = "한 페이지당 불러올 콘텐츠 개수", dataType = "integer", paramType = "query", defaultValue = "10"),
            @ApiImplicitParam(name = "sort", value = "정렬방법(id,desc,asc)", dataType = "string", paramType = "query", defaultValue = "asc")
    })
    @GetMapping("")
    public ResponseEntity<OffVolunteerPageResponse> getPagingOffVolunteer(@PageableDefault Pageable pageable,
                                                                      @RequestParam(name="searchOption", required = false, defaultValue = "title") SearchOption option,
                                                                      @RequestParam(name="keyword", required = false) String keyword){
        Page<OffVolunteer> offVolunteers;
        String searchOption = option.getValue();
        List<OffVolunteerListResponse> offVolunteerListResponseList = new ArrayList<>();
        OffVolunteerPageResponse offVolunteerPageResponse;

        if(keyword==null){
            offVolunteers = offVolunteerService.findAll(pageable);
            addOffVolunteerListResponse(offVolunteers, offVolunteerListResponseList);
            offVolunteerPageResponse = new OffVolunteerPageResponse(pageable, offVolunteers, option, null, offVolunteerListResponseList);
        }else {
            if(searchOption=="제목+내용"){
                offVolunteers = offVolunteerService.findByTitleOrContents(pageable, keyword, keyword);
            }else {
                offVolunteers = offVolunteerService.findByTitle(pageable, keyword);
            }
            if (offVolunteers.getContent().isEmpty()) {
                OffVolunteerListResponse.addNullOffVolunteerListResponse(offVolunteerListResponseList);
            } else {
                addOffVolunteerListResponse(offVolunteers, offVolunteerListResponseList);
            }
            offVolunteerPageResponse = new OffVolunteerPageResponse(pageable, offVolunteers, option, keyword, offVolunteerListResponseList);
        }
        return new ResponseEntity<>(offVolunteerPageResponse, HttpStatus.OK);
    }

    private void addOffVolunteerListResponse(Page<OffVolunteer> offVolunteers, List<OffVolunteerListResponse> offVolunteerListResponseList){
        if(offVolunteers.getSize()>=offVolunteers.getTotalElements()){
            for(int i=0; i<offVolunteers.getTotalElements(); i++){
                OffVolunteerListResponse offVolunteerListResponse = OffVolunteerListResponse.builder()
                        .idx(offVolunteers.getContent().get(i).getIdx())
                        .title(offVolunteers.getContent().get(i).getTitle())
                        .createdDate(offVolunteers.getContent().get(i).getCreatedDate())
                        .build();
                offVolunteerListResponseList.add(offVolunteerListResponse);
            }
        }else {
            int contentSize = offVolunteers.getSize()*offVolunteers.getNumber();
            for(int i=0; i<offVolunteers.getSize();i++){
                if(contentSize>=offVolunteers.getTotalElements())
                    break;
                OffVolunteerListResponse offVolunteerListResponse = OffVolunteerListResponse.builder()
                        .idx(offVolunteers.getContent().get(i).getIdx())
                        .title(offVolunteers.getContent().get(i).getTitle())
                        .createdDate(offVolunteers.getContent().get(i).getCreatedDate())
                        .build();
                offVolunteerListResponseList.add(offVolunteerListResponse);
                contentSize++;
            }
        }
    }
}
