package com.deu.synabro.controller;

import com.deu.synabro.entity.VolunteerWork;
import com.deu.synabro.entity.Work;
import com.deu.synabro.entity.enums.SearchOption;
import com.deu.synabro.http.request.VolunteerWorkUpdateRequest;
import com.deu.synabro.http.response.*;
import com.deu.synabro.service.*;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 봉사 수행에 대한 CRUD 메소드들이 정의된 클래스입니다.
 *
 * @author tkfdkskarl56
 * @since 1.0
 */
@Tag(name="VolunteerWork", description = "봉사 수행 API")
@RestController
@RequestMapping("/api/volunteerWorks")
@AllArgsConstructor
public class VolunteerWorkController {

    @Autowired
    VolunteerWorkService volunteerWorkService;

    @Autowired
    WorkService workService;

    @Autowired
    MemberService memberService;

    private static final String DELETE_VOLUNTEER_WORK = "{\n" +
            "    \"code\" : 204\n" +
            "    \"message\" : \"봉사 수행글이 삭제되었습니다.\"\n" +
            "}";

    private static final String NOT_VOLUNTEER_WORK = "{\n" +
            "    \"code\" : 404\n" +
            "    \"message\" : \"봉사 수행글이 없습니다.\"\n" +
            "}";

    private static final String CREATE_VOLUNTEER_WORK = "{\n" +
            "    \"code\" : 200\n" +
            "    \"message\" : \"봉사 수행글이 생성되었습니다.\"\n" +
            "}";

    private static final String UPDATE_VOLUNTEER_WORK = "{\n" +
            "    \"code\" : 200\n" +
            "    \"message\" : \"봉사 수행글이 수정되었습니다.\"\n" +
            "}";

    /**
     * 봉사 수행글을 찾는 GET API 입니다.
     *
     * @param uuid 봉사 수행글의 uuid 입니다.
     * @return uuid 값으로 찾은 봉사 수행글을 반환합니다.
     */
    @Operation(tags = "VolunteerWork", summary = "id 값으로 봉사 수행글을 찾습니다.",
            responses={
                    @ApiResponse(responseCode = "200", description = "id 값으로 봉사 수행글 정보 조회 성공",
                            content = @Content(schema = @Schema(implementation = VolunteerWorkResponse.class)))
            })
    @GetMapping("/{volunteer_work_id}")
    public ResponseEntity<VolunteerWorkResponse> getVolunteerWork(@Parameter(description = "고유 아이디")
                                                                @PathVariable(name = "volunteer_work_id") UUID uuid){
        try{
            VolunteerWork volunteerWork = volunteerWorkService.findByIdx(uuid);
            return new ResponseEntity<>(volunteerWorkService.getVolunteerResponse(volunteerWork),HttpStatus.OK);
        }catch ( NullPointerException e){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 제목, 제목+내용으로 봉사 수행글을 찾아주는 GET API 입니다.
     *
     * @param pageable 페이징처리 객체
     * @param option (제목, 제목+내용)를 입력받습니다.
     * @param keyword 검색할 단어를 입력받습니다.
     * @return 제목이나 제목+내용으로 검색한 봉사 수행글을 반환합니다.
     */
    @Operation(tags = "VolunteerWork", summary = "제목, 제목+내용으로 봉사 수행 글을 찾습니다.",
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
    @GetMapping("")
    public ResponseEntity<VolunteerWorkPageResponse> getVolunteerWorks(@PageableDefault Pageable pageable,
                                                              @RequestParam(name="searchOption", required = false, defaultValue = "title") SearchOption option,
                                                              @RequestParam(name="keyword", required = false) String keyword){
        Page<VolunteerWork> volunteerWorks;
        String searchOption = option.getValue();
        List<VolunteerListResponse> volunteerListResponseList = new ArrayList<>();
        VolunteerWorkPageResponse volunteerWorkPageResponse;

        if(keyword==null){
            volunteerWorks = volunteerWorkService.findAll(pageable);
            addVolunteerListResponse(volunteerWorks, volunteerListResponseList);
            volunteerWorkPageResponse = new VolunteerWorkPageResponse(pageable, volunteerWorks, option, null, volunteerListResponseList);
        }else {
            if(searchOption=="제목+내용"){
                volunteerWorks = volunteerWorkService.findByTitleOrContents(pageable, keyword, keyword);
            }else {
                volunteerWorks = volunteerWorkService.findByTitle(pageable, keyword);
            }
            if (volunteerWorks.getContent().isEmpty()) {
                VolunteerListResponse.addNullVolunteerListResponse(volunteerListResponseList);
            } else {
                addVolunteerListResponse(volunteerWorks, volunteerListResponseList);
            }
            volunteerWorkPageResponse = new VolunteerWorkPageResponse(pageable, volunteerWorks, option, keyword, volunteerListResponseList);
        }
        return new ResponseEntity<>(volunteerWorkPageResponse, HttpStatus.OK);
    }

    /**
     * 페이징 처리할 봉사 수행글을 추가해주는 메소드입니다.
     *
     * @param volunteerWorks 페이징 처리할 봉사 수행글을 입력합니다.
     * @param volunteerListResponseList 페이징 처리된 봉사 요청글을 추가할 리스트를 입력합니다.
     */
    private void addVolunteerListResponse(Page<VolunteerWork> volunteerWorks, List<VolunteerListResponse> volunteerListResponseList){
        if(volunteerWorks.getSize()>=volunteerWorks.getTotalElements()){
            for(int i=0; i<volunteerWorks.getTotalElements(); i++){
                VolunteerListResponse volunteerListResponse = VolunteerListResponse.builder()
                        .idx(volunteerWorks.getContent().get(i).getIdx())
                        .title(volunteerWorks.getContent().get(i).getWorkId().getTitle())
                        .endedDate(volunteerWorks.getContent().get(i).getWorkId().getEndedDate())
                        .build();
                volunteerListResponseList.add(volunteerListResponse);
            }
        }else {
            int contentSize = volunteerWorks.getSize()*volunteerWorks.getNumber();
            for(int i=0; i<volunteerWorks.getSize();i++){
                if(contentSize>=volunteerWorks.getTotalElements())
                    break;
                VolunteerListResponse volunteerListResponse = VolunteerListResponse.builder()
                        .idx(volunteerWorks.getContent().get(i).getIdx())
                        .title(volunteerWorks.getContent().get(i).getWorkId().getTitle())
                        .endedDate(volunteerWorks.getContent().get(i).getWorkId().getEndedDate())
                        .build();
                volunteerListResponseList.add(volunteerListResponse);
                contentSize++;
            }
        }
    }

    /**
     * 봉사 수행글을 생성하는 POST API 입니다
     *
     * @param uuid Work의 uuid 값 입니다.
     * @return 봉사 수행글의 생성 상태를 반환합니다.
     */
    @Operation(tags = "VolunteerWork", summary = "봉사 수행글을 생성합니다.",
            responses={
                    @ApiResponse(responseCode = "200", description = "봉사 수행글 생성 성공",
                            content = @Content(schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(value = CREATE_VOLUNTEER_WORK)))
            })
    @PostMapping("{work_id}")
    public ResponseEntity<GeneralResponse> createVolunteerWork(@Parameter(description = "고유 아이디")
                                                               @PathVariable(name = "work_id") UUID uuid){
        Work work = workService.findByIdx(uuid);
        UUID userId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
        volunteerWorkService.setVolunteerWork(work, userId);
        return new ResponseEntity<>(GeneralResponse.of(HttpStatus.OK,"봉사 수행글이 생성되었습니다."), HttpStatus.OK);
    }

    /**
     * uuid 값으로 수정할 봉사 수행글을 찾고 봉사 수행글을 수정하게 해주는 PATCH API 입니다.
     *
     * @param uuid 봉사 수행글의 uuid 입니다.
     * @param volunteerWorkUpdateRequest 수정할 봉사 수행 내용 (내용) 입니다.
     * @return 봉사 수행글의 수정 상태를 반환합니다.
     */
    @Operation(tags = "VolunteerWork", summary = "봉사 수행글을 수정합니다.",
            responses={
                    @ApiResponse(responseCode = "200", description = "봉사 수행글 수정 성공",
                           content = @Content(schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(value = UPDATE_VOLUNTEER_WORK))),
                    @ApiResponse(responseCode = "404", description = "삭제할 봉사 수행글이 없음",
                            content = @Content(schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(value = NOT_VOLUNTEER_WORK)))
            })
    @PatchMapping("{volunteer_work_id}")
    public ResponseEntity<GeneralResponse> updateVolunteerWork(@Parameter(description = "고유 아이디")
                                                             @PathVariable(name = "volunteer_work_id") UUID uuid,
                                                             @Parameter @RequestBody VolunteerWorkUpdateRequest volunteerWorkUpdateRequest){
        try{
            VolunteerWork volunteerWork = volunteerWorkService.findByIdx(uuid);
            UUID userId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
            volunteerWorkService.updateVolunteerWork(volunteerWorkUpdateRequest, volunteerWork, userId);
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.OK, "봉사 수행글이 수정되었습니다"), HttpStatus.OK);
        } catch (NullPointerException e){
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.NOT_FOUND,"수정할 봉사 수행글이 없습니다."), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * uuid 값으로 봉사 수행글을 삭제하는 DELETE API 입니다.
     *
     * @param uuid 봉사 수행글의 uuid 입니다.
     * @return 봉사 수행글의 삭제 상태를 반환합니다.
     */
    @Operation(tags = "VolunteerWork", summary = "봉사 수행글을 삭제 합니다.",
            responses={
                    @ApiResponse(responseCode = "204", description = "봉사 수행글 삭제 성공",
                            content = @Content(schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(value = DELETE_VOLUNTEER_WORK))),
                    @ApiResponse(responseCode = "404", description = "삭제할 봉사 수행글이 없음",
                            content = @Content(schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(value = NOT_VOLUNTEER_WORK)))
            })
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping("{volunteer_work_id}")
    public ResponseEntity<GeneralResponse> deleteVolunteerWork(@Parameter(description = "고유 아이디")
                                                               @PathVariable(name = "volunteer_work_id") UUID uuid){
        try{
            volunteerWorkService.deleteById(uuid);
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.OK,"봉사 수행글이 삭제되었습니다."), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.NOT_FOUND,"삭제할 봉사 수행글이 없습니다."), HttpStatus.NOT_FOUND);
        }
    }
}
