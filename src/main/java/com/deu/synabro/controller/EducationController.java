package com.deu.synabro.controller;

import com.deu.synabro.http.request.EducationRequest;
import com.deu.synabro.http.response.GeneralResponse;
import com.deu.synabro.http.response.education.EducationDetailResponse;
import com.deu.synabro.http.response.education.EducationHomeResponse;
import com.deu.synabro.service.EducationService;
import com.deu.synabro.util.FileUtil;
import com.google.common.net.HttpHeaders;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@Tag(name = "Education", description = "교육 데이터 관련 API")
@RequestMapping("/api/educations")
@RestController
public class EducationController {
    private final EducationService educationService;

    public EducationController(EducationService educationService) {
        this.educationService = educationService;
    }

    @GetMapping(value = "/test")
    public ResponseEntity<?> downloadTest() {
        FileUtil fileUtil = new FileUtil();

        Resource resource = null;
        try {
            resource = fileUtil.downloadFile("Study-Guide.pdf");
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }

        if (resource == null) {
            return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
        }

        String contentType = "application/octet-stream";
        String headerValue = "attachment; filename=\"" + "Study-Guide.pdf" + "\"";

        return ResponseEntity.ok()
                             .contentType(MediaType.parseMediaType(contentType))
                             .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                             .body(resource);
    }

    @Operation(summary = "교육 홈 화면 조회", description = "교육 메인페이지에 대한 정보를 조회합니다.", tags = "Education",
            responses = {
                    @ApiResponse(responseCode = "200", description = "교육 정보 조회 성공",
                            content = {@Content(array = @ArraySchema(schema = @Schema(implementation = EducationHomeResponse.class)))})
            })
    @GetMapping(value = "/home")
    public ResponseEntity<EducationHomeResponse> getHome() {
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.ok(new EducationHomeResponse(educationService.getBoards()));
    }

    @Operation(summary = "교육 진행 상세정보 조회", description = "사용자가 진행할 교육에 대한 내용을 조회합니다.", tags = "Education",
            responses = {
                    @ApiResponse(responseCode = "200", description = "교육 정보 조회 성공",
                            content = {@Content(array = @ArraySchema(schema = @Schema(implementation = EducationDetailResponse.class)))})
            })
    @GetMapping(value = "/{id}")
    public ResponseEntity<EducationDetailResponse> getEducation(@PathVariable(name = "id") UUID id) {
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.ok(new EducationDetailResponse());
    }

    @Operation(summary = "교육 자료 추가", description = "교육을 진행한 상황을 저장합니다.", tags = "Education",
            responses = {
                    @ApiResponse(responseCode = "200", description = "교육 진행 상황 저장 성공",
                            content = {@Content(array = @ArraySchema(schema = @Schema(implementation = GeneralResponse.class)))})
            })
    @PostMapping(value = "/{id}")
    public ResponseEntity<?> addEducation(@PathVariable(name = "id") UUID id, @RequestBody EducationRequest educationRequest) {
        return new ResponseEntity<>(GeneralResponse.of(HttpStatus.OK, "사용자 등록에 성공하였습니다."), HttpStatus.OK);
    }
}
