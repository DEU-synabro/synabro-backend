package com.deu.synabro.controller;

import com.deu.synabro.entity.Certification;
import com.deu.synabro.entity.enums.SearchOption;
import com.deu.synabro.http.request.CertificationRequest;
import com.deu.synabro.http.response.*;
import com.deu.synabro.service.CertificationService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Tag(name="Certification", description = "봉사 인증 API")
@RestController
@RequestMapping("/api/certification")
public class CertificationController {
    @Autowired
    CertificationService certificationService;

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
            "    \"message\" : \"봉사 수혜자나 관리자만 신청할 수 있습니다.\"\n" +
            "}";

    @PostMapping("")
    public ResponseEntity<GeneralResponse> createCertification(@Parameter @RequestPart(name = "tagName") String tagName ,
                                                               @Parameter @RequestPart(name = "certificationRequest")CertificationRequest certificationRequest,
                                                               @Parameter @RequestPart MultipartFile file){
        try{
            certificationService.setCertificationRepository(certificationRequest, tagName, fileUtil.saveDocs(file));
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.OK,"봉사 인증글이 생성되었습니다."), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.BAD_REQUEST,"봉사 인증글이 생성이 실패하였습니다."), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(tags = "certification", summary = "봉사 요청글을 삭제 합니다.",
            responses={
                    @ApiResponse(responseCode = "204", description = "봉사 요청글 삭제 성공",
                            content = @Content(schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(value = DELETE_WORKS))),
                    @ApiResponse(responseCode = "404", description = "삭제할 봉사 요청글이 없음",
                            content = @Content(schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(value = NOT_WORKS)))
            })
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping("{certification_id}")
    public ResponseEntity<GeneralResponse> deleteCertification(@Parameter @PathVariable(name = "certifiacation_id") UUID uuid){
        try{
            certificationService.deleteById(uuid);
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.OK,"봉사 요청글이 삭제되었습니다."), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.NOT_FOUND,"삭제할 봉사 요청글이 없습니다."), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(tags = "certification", summary = "봉사 요청글을 수정합니다.",
            responses={
                    @ApiResponse(responseCode = "200", description = "봉사 요청글 수정 성공",
                            content = @Content(schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(value = UPDATE_WORKS))),
                    @ApiResponse(responseCode = "404", description = "삭제할 봉사 요청 글이 없음",
                            content = @Content(schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(value = NOT_WORKS)))
            })
    @PatchMapping("/{certification_id}")
    public ResponseEntity<GeneralResponse> updateCertification(@Parameter(description = "고유 아이디") @PathVariable(name = "certification_id") UUID uuid,
                                                               @Parameter @RequestBody CertificationRequest certificationRequest){
        try{
            Certification certification = certificationService.findByIdx(uuid);
            certificationService.updateCertification(certificationRequest, certification);
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.OK, "봉사 요청글이 수정되었습니다"), HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.NOT_FOUND,"수정할 봉사 요청글이 없습니다."), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(tags = "Work", summary = "id 값으로 봉사 요청글을 찾습니다.",
            responses={
                    @ApiResponse(responseCode = "200", description = "id 값으로 봉사 요청글 정보 조회 성공",
                            content = @Content(schema = @Schema(implementation = WorkResponse.class)))
            })
    @GetMapping("/{work_id}")
    public ResponseEntity<CertificationResponse> getContents(@Parameter(description = "고유 아이디")
                                                    @PathVariable(name = "work_id") UUID uuid){
        try{
            Certification certification = certificationService.findByIdx(uuid);
            return new ResponseEntity<>(certificationService.getCertification(certification), HttpStatus.OK);
        }catch ( NullPointerException e){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
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
    public ResponseEntity<CertificationPageResponse> getPagingCertification(@PageableDefault Pageable pageable,
                                                              @RequestParam(name="searchOption", required = false, defaultValue = "title") SearchOption option,
                                                              @RequestParam(name="keyword", required = false) String keyword){
        Page<Certification> certifications;
        String searchOption = option.getValue();
        List<CertificationListResponse> certificationListResponseList = new ArrayList<>();
        CertificationPageResponse certificationPageResponse;

        if(keyword==null){
            certifications = certificationService.findAll(pageable);
            addCertificationListResponse(certifications, certificationListResponseList);
            certificationPageResponse = new CertificationPageResponse(pageable, certifications, option, null, certificationListResponseList);
        }else {
            if(searchOption=="제목+내용"){
                certifications = certificationService.findByTitleOrContents(pageable, keyword, keyword);
            }else {
                certifications = certificationService.findByTitle(pageable, keyword);
            }
            if (certifications.getContent().isEmpty()) {
                CertificationListResponse.addNullCertificationListResponse(certificationListResponseList);
            } else {
                addCertificationListResponse(certifications, certificationListResponseList);
            }
            certificationPageResponse = new CertificationPageResponse(pageable, certifications, option, keyword, certificationListResponseList);
        }
        return new ResponseEntity<>(certificationPageResponse, HttpStatus.OK);
    }

    /**
     * 페이징 처리할 봉사 요청글을 추가해주는 메소드입니다.
     *
     * @param certifications 페이징 처리할 봉사 요청글을 입력받습니다.
     * @param contentsResponseList 페이징 처리된 봉사 요청글을 추가할 리스트를 입력받습니다.
     */
    private void addCertificationListResponse(Page<Certification> certifications, List<CertificationListResponse> contentsResponseList){
        if(certifications.getSize()>=certifications.getTotalElements()){
            for(int i=0; i<certifications.getTotalElements(); i++){
                CertificationListResponse certificationListResponse = CertificationListResponse.builder()
                        .idx(certifications.getContent().get(i).getIdx())
                        .title(certifications.getContent().get(i).getTitle())
                        .createdDate(certifications.getContent().get(i).getCreatedDate())
                        .build();
                contentsResponseList.add(certificationListResponse);
            }
        }else {
            int contentSize = certifications.getSize()*certifications.getNumber();
            for(int i=0; i<certifications.getSize();i++){
                if(contentSize>=certifications.getTotalElements())
                    break;
                CertificationListResponse certificationListResponse = CertificationListResponse.builder()
                        .idx(certifications.getContent().get(i).getIdx())
                        .title(certifications.getContent().get(i).getTitle())
                        .createdDate(certifications.getContent().get(i).getCreatedDate())
                        .build();
                contentsResponseList.add(certificationListResponse);
                contentSize++;
            }
        }
    }

    /**
     * 봉사 인증글에 있는 사진을 다운로드 해주는 메소드입니다.
     *
     * @param uuid 봉사 인증글의 uuid 값입니다,
     * @return 봉사 인증글의 사진을 반환합니다.
     */
    @CrossOrigin(origins = "*", exposedHeaders = {"Content-Disposition"}, maxAge = 3600)
    @GetMapping("/download/{certification_id}")
    public ResponseEntity<Object> download(@Parameter(description = "고유 아이디")
                                           @PathVariable(name = "certification_id") UUID uuid)  {
        return fileUtil.downDocs(uuid);
    }
}
