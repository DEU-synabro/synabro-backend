package com.deu.synabro.controller;

import com.deu.synabro.entity.Inspection;
import com.deu.synabro.entity.Member;
import com.deu.synabro.entity.VolunteerWork;
import com.deu.synabro.entity.enums.SearchOption;
import com.deu.synabro.http.request.InspectionUpdateRequest;
import com.deu.synabro.http.response.*;
import com.deu.synabro.service.InspectionService;
import com.deu.synabro.service.MemberService;
import com.deu.synabro.service.VolunteerWorkService;
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
import java.util.Optional;
import java.util.UUID;

/**
 * 봉사 검수에 대한 CRUD 메소드들이 정의된 클래스입니다.
 *
 * @author tkfdkskarl56
 * @since 1.0
 */
@Tag(name="Inspection", description = "검수 API")
@RestController
@RequestMapping("/api/inspections")
@AllArgsConstructor
public class InspectionController {

    @Autowired
    InspectionService inspectionService;

    @Autowired
    VolunteerWorkService volunteerWorkService;

    @Autowired
    MemberService memberService;

    private static final String DELETE_INSPECTION = "{\n" +
            "    \"code\" : 204\n" +
            "    \"message\" : \"봉사 검수글이 삭제되었습니다.\"\n" +
            "}";

    private static final String NOT_FOUND_INSPECTION = "{\n" +
            "    \"code\" : 404\n" +
            "    \"message\" : \"봉사 검수글이 없습니다.\"\n" +
            "}";

    private static final String CREATE_INSPECTION = "{\n" +
            "    \"code\" : 200\n" +
            "    \"message\" : \"봉사 검수글이 생성되었습니다.\"\n" +
            "}";

    private static final String UPDATE_INSPECTION = "{\n" +
            "    \"code\" : 200\n" +
            "    \"message\" : \"봉사 수행글이 수정되었습니다.\"\n" +
            "}";

    private static final String FORBIDDEN_WORKS = "{\n" +
            "    \"code\" : 403\n" +
            "    \"message\" : \"본인 글은 검수할 수 없습니다.\"\n" +
            "}";

    /**
     * 봉사 검수글을 생성하는 POST API 입니다
     *
     * @param uuid VolunteerWork의 uuid 값 입니다.
     * @return 봉사 검수글의 생성 상태를 반환합니다.
     */
    @Operation(tags = "Inspection", summary = "봉사 검수글을 생성합니다.",
            responses={
                    @ApiResponse(responseCode = "200", description = "봉사 검수글 생성 성공",
                            content = @Content(schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(value = CREATE_INSPECTION))),
                    @ApiResponse(responseCode = "403", description = "권한이 없습니다.",
                            content = @Content(schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(value = FORBIDDEN_WORKS)))
            })
    @PostMapping("{volunteer_work_id}")
    public ResponseEntity<GeneralResponse> setInspection(@Parameter(description = "고유 아이디") @PathVariable(name = "volunteer_work_id") UUID uuid){
        VolunteerWork volunteerWork = volunteerWorkService.findByIdx(uuid);
        UUID userId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());

        if(volunteerWork.getUserId().getIdx().equals(userId)){
            inspectionService.setInspection(volunteerWork, userId);
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.OK,"봉사 검수 글이 생성되었습니다."), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.FORBIDDEN,"본인 글만 검수 요청할 수 없습니다."), HttpStatus.FORBIDDEN);
        }
    }

    /**
     * 봉사 검수글을 찾는 GET API 입니다.
     *
     * @param uuid 봉사 검수글의 uuid 입니다.
     * @return uuid 값으로 찾은 봉사 검수글을 반환합니다.
     */
    @Operation(tags = "Inspection", summary = "id 값으로 검수글을 찾습니다.",
            responses={
                    @ApiResponse(responseCode = "200", description = "id 값으로 검수글 정보 조회 성공",
                            content = @Content(schema = @Schema(implementation = InspectionResponse.class)))
            })
    @GetMapping("{inspection_id}")
    public ResponseEntity<InspectionResponse> getInspection(@Parameter(description = "고유아이디") @PathVariable(name="inspection_id") UUID uuid){
        try{
            Inspection inspection = inspectionService.findByIdx(uuid);
            return new ResponseEntity<>(inspectionService.getInspectionResponse(inspection),HttpStatus.OK);
        }catch ( NullPointerException e){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * uuid 값으로 수정할 봉사 검수글을 찾고 봉사 수행글을 수정하게 해주는 PATCH API 입니다.
     *
     * @param uuid 봉사 검수글의 uuid 입니다.
     * @param inspectionUpdateRequest 수정할 봉사 검수 내용 (봉사 시간, 내용, 봉사 마감일) 입니다.
     * @return 봉사 검수글의 수정 상태를 반환합니다.
     */
    @Operation(tags = "Inspection", summary = "봉사 검수글을 수정합니다.",
            responses={
                    @ApiResponse(responseCode = "200", description = "봉사 검수글 수정 성공",
                            content = @Content(schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(value = UPDATE_INSPECTION))),
                    @ApiResponse(responseCode = "404", description = "삭제할 봉사 검수글이 없음",
                            content = @Content(schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(value = NOT_FOUND_INSPECTION)))
            })
    @PatchMapping("{inspection_id}")
    public ResponseEntity<GeneralResponse> updateInspection(@Parameter(description = "고유 아이디")
                                                               @PathVariable(name = "inspection_id") UUID uuid,
                                                               @Parameter @RequestBody InspectionUpdateRequest inspectionUpdateRequest){
        try{
            Inspection inspection = inspectionService.findByIdx(uuid);
            UUID userId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
            inspectionService.updateInspection(inspectionUpdateRequest, inspection, userId);
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.OK, "봉사 검수글이 수정되었습니다"), HttpStatus.OK);
        } catch (NullPointerException e){
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.NOT_FOUND,"수정할 봉사 수행글이 없습니다."), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * uuid 값으로 봉사 검수글을 삭제하는 DELETE API 입니다.
     *
     * @param uuid 봉사 검수글의 uuid 입니다.
     * @return 봉사 검수글의 삭제 상태를 반환합니다.
     */
    @Operation(tags = "Inspection", summary = "봉사 검수글을 삭제 합니다.",
            responses={
                    @ApiResponse(responseCode = "204", description = "봉사 검수글 삭제 성공",
                            content = @Content(schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(value = DELETE_INSPECTION))),
                    @ApiResponse(responseCode = "404", description = "삭제할 봉사 수행글이 없음",
                            content = @Content(schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(value = NOT_FOUND_INSPECTION)))
            })
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping("{inspection_id}")
    public ResponseEntity<GeneralResponse> deleteInspection(@Parameter(description = "고유 아이디")
                                                                @PathVariable(name = "inspection_id") UUID uuid){
        try{
            inspectionService.deleteById(uuid);
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.OK,"봉사 검수글이 삭제되었습니다."), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.NOT_FOUND,"삭제할 봉사 검수글이 없습니다."), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 제목, 제목+내용으로 봉사 검수글을 찾아주는 GET API 입니다.
     *
     * @param pageable 페이징처리 객체
     * @param option (제목, 제목+내용)를 입력받습니다.
     * @param keyword 검색할 단어를 입력받습니다.
     * @return 제목이나 제목+내용으로 검색한 봉사 검수글을 반환합니다.
     */
    @Operation(tags = "Inspection", summary = "제목, 제목+내용으로 봉사 검수글을 찾습니다.",
            responses={
                    @ApiResponse(responseCode = "200", description = "제목, 제목+내용으로 글 정보 조회 성공",
                            content = @Content(schema = @Schema(implementation = InspectionPageResponse.class)))
            })
    @io.swagger.annotations.ApiResponses(
            @io.swagger.annotations.ApiResponse(
                    response = InspectionPageResponse.class, message = "ok", code=200)
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "페이지 번호", dataType = "integer", paramType = "query", defaultValue = "0"),
            @ApiImplicitParam(name = "size", value = "한 페이지당 불러올 콘텐츠 개수", dataType = "integer", paramType = "query", defaultValue = "10"),
            @ApiImplicitParam(name = "sort", value = "정렬방법(id,desc,asc)", dataType = "string", paramType = "query", defaultValue = "asc")
    })
    @GetMapping("")
    public ResponseEntity<InspectionPageResponse> getInspections(@PageableDefault Pageable pageable,
                                                                       @RequestParam(name="searchOption", required = false, defaultValue = "title") SearchOption option,
                                                                       @RequestParam(name="keyword", required = false) String keyword){
        Page<Inspection> inspections;
        String searchOption = option.getValue();
        List<InspectionListResponse> inspectionListResponseList = new ArrayList<>();
        InspectionPageResponse inspectionPageResponse;

        if(keyword==null){
            inspections = inspectionService.findAll(pageable);
            addInspectionListResponse(inspections, inspectionListResponseList);
            inspectionPageResponse = new InspectionPageResponse(pageable, inspections, option, null, inspectionListResponseList);
        }else {
            if(searchOption=="제목+내용"){
                inspections = inspectionService.findByTitleOrContents(pageable, keyword, keyword);
            }else {
                inspections = inspectionService.findByTitle(pageable, keyword);
            }
            if (inspections.getContent().isEmpty()) {
                InspectionListResponse.addNullInspectionListResponse(inspectionListResponseList);
            } else {
                addInspectionListResponse(inspections, inspectionListResponseList);
            }
            inspectionPageResponse = new InspectionPageResponse(pageable, inspections, option, keyword, inspectionListResponseList);
        }
        return new ResponseEntity<>(inspectionPageResponse, HttpStatus.OK);
    }

    /**
     * 페이징 처리할 봉사 검수글을 추가해주는 메소드입니다.
     *
     * @param inspections 페이징 처리할 봉사 검수글을 입력합니다.
     * @param inspectionListResponseList 페이징 처리된 봉사 검수글을 추가할 리스트를 입력합니다.
     */
    private void addInspectionListResponse(Page<Inspection> inspections, List<InspectionListResponse> inspectionListResponseList){
        if(inspections.getSize()>=inspections.getTotalElements()){
            for(int i=0; i<inspections.getTotalElements(); i++){
                InspectionListResponse inspectionListResponse = InspectionListResponse.builder()
                        .idx(inspections.getContent().get(i).getIdx())
                        .title(inspections.getContent().get(i).getVolunteerWorkId().getWorkId().getTitle())
                        .endedDate(inspections.getContent().get(i).getVolunteerWorkId().getWorkId().getEndedDate())
                        .build();
                inspectionListResponseList.add(inspectionListResponse);
            }
        }else {
            int contentSize = inspections.getSize()*inspections.getNumber();
            for(int i=0; i<inspections.getSize();i++){
                if(contentSize>=inspections.getTotalElements())
                    break;
                InspectionListResponse inspectionListResponse = InspectionListResponse.builder()
                        .idx(inspections.getContent().get(i).getIdx())
                        .title(inspections.getContent().get(i).getVolunteerWorkId().getWorkId().getTitle())
                        .endedDate(inspections.getContent().get(i).getVolunteerWorkId().getWorkId().getEndedDate())
                        .build();
                inspectionListResponseList.add(inspectionListResponse);
                contentSize++;
            }
        }
    }
}
