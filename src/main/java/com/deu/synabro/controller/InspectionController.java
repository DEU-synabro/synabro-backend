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

    @Operation(tags = "Inspection", summary = "봉사 검수글을 생성합니다.",
            responses={
                    @ApiResponse(responseCode = "200", description = "봉사 검수글 생성 성공",
                            content = @Content(schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(value = CREATE_INSPECTION)))
            })
    @PostMapping("{volunteer_work_id}")
    public ResponseEntity<GeneralResponse> setInspection(@Parameter(description = "고유 아이디") @PathVariable(name = "volunteer_work_id") UUID uuid){
        VolunteerWork volunteerWork = volunteerWorkService.findById(uuid);
        inspectionService.setInspection(volunteerWork);
        return new ResponseEntity<>(GeneralResponse.of(HttpStatus.OK,"봉사 검수 글이 생성되었습니다."), HttpStatus.OK);
    }

    @Operation(tags = "Inspection", summary = "id 값으로 검수글을 찾습니다.",
            responses={
                    @ApiResponse(responseCode = "200", description = "id 값으로 검수글 정보 조회 성공",
                            content = @Content(schema = @Schema(implementation = InspectionResponse.class)))
            })
    @GetMapping("{inspection_id}")
    public ResponseEntity<InspectionResponse> getInspection(@Parameter(description = "고유아이디") @PathVariable(name="inspection_id") UUID uuid){
        InspectionResponse inspectionResponse;
        try{
            inspectionResponse = inspectionService.findByIdAndGetResponse(uuid);
            return new ResponseEntity<>(inspectionResponse,HttpStatus.OK);
        }catch (IllegalArgumentException e){
            inspectionResponse = inspectionService.getNullResponse();
            return new ResponseEntity<>(inspectionResponse,HttpStatus.NOT_FOUND);
        }
    }

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
        Inspection inspection = inspectionService.findById(uuid);
        if(inspection==null){
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.NOT_FOUND,"수정할 봉사 검수글이 없습니다."), HttpStatus.NOT_FOUND);
        }else {
            UUID userId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
            inspectionService.updateInspection(inspectionUpdateRequest, inspection, userId);
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.OK, "봉사 검수글이 수정되었습니다"), HttpStatus.OK);
        }
    }

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
        if(inspectionService.deleteById(uuid)){
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.OK,"봉사 검수글이 삭제되었습니다."), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.NOT_FOUND,"삭제할 봉사 검수글이 없습니다."), HttpStatus.NOT_FOUND);
        }
    }

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
        InspectionListResponse inspectionListResponse;
        InspectionPageResponse inspectionPageResponse;

        if(keyword==null){
            inspections = inspectionService.findAll(pageable);
            if(inspections.getSize()>=inspections.getTotalElements()){
                for(int i=0; i<inspections.getTotalElements(); i++){
                    inspectionListResponse = InspectionListResponse.builder()
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
                    inspectionListResponse = InspectionListResponse.builder()
                            .idx(inspections.getContent().get(i).getIdx())
                            .title(inspections.getContent().get(i).getVolunteerWorkId().getWorkId().getTitle())
                            .endedDate(inspections.getContent().get(i).getVolunteerWorkId().getWorkId().getEndedDate())
                            .build();
                    inspectionListResponseList.add(inspectionListResponse);
                    contentSize++;
                }
            }
            inspectionPageResponse = new InspectionPageResponse(pageable, inspections, option, null, inspectionListResponseList);
            return new ResponseEntity<>(inspectionPageResponse, HttpStatus.OK);
        }else {
            if(searchOption=="제목+내용"){
                inspections = inspectionService.findByTitleOrContents(pageable, keyword, keyword);
            }else {
                inspections = inspectionService.findByTitle(pageable, keyword);
            }
            if (inspections.getContent().isEmpty()) {
                inspectionListResponse = InspectionListResponse.builder()
                        .idx(null)
                        .title(null)
                        .endedDate(null)
                        .build();
                inspectionListResponseList.add(inspectionListResponse);
            } else {
                if(inspections.getSize()>=inspections.getTotalElements()){
                    for(int i=0; i<inspections.getTotalElements(); i++){
                        inspectionListResponse = InspectionListResponse.builder()
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
                        inspectionListResponse = InspectionListResponse.builder()
                                .idx(inspections.getContent().get(i).getIdx())
                                .title(inspections.getContent().get(i).getVolunteerWorkId().getWorkId().getTitle())
                                .endedDate(inspections.getContent().get(i).getVolunteerWorkId().getWorkId().getEndedDate())
                                .build();
                        inspectionListResponseList.add(inspectionListResponse);
                        contentSize++;
                    }
                }
            }
            inspectionPageResponse = new InspectionPageResponse(pageable, inspections, option, null, inspectionListResponseList);
            return new ResponseEntity<>(inspectionPageResponse, HttpStatus.OK);
        }
    }
}
