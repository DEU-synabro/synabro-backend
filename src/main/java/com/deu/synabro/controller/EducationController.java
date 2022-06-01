package com.deu.synabro.controller;

import com.deu.synabro.http.request.EducationRequest;
import com.deu.synabro.http.response.GeneralResponse;
import com.deu.synabro.http.response.education.EducationDetailResponse;
import com.deu.synabro.http.response.education.EducationHomeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Education", description = "교육 데이터 관련 API")
@RequestMapping("/api/educations")
@RestController
public class EducationController {

    @Operation(summary = "교육 홈 화면 조회", description = "교육 메인페이지에 대한 정보를 조회합니다.", tags = "Education",
            responses = {
                    @ApiResponse(responseCode = "200", description = "교육 정보 조회 성공",
                            content = {@Content(array = @ArraySchema(schema = @Schema(implementation = EducationHomeResponse.class)))})
            })
    @GetMapping(value = "/home")
    public ResponseEntity<EducationHomeResponse> getHome() {
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.ok(new EducationHomeResponse());
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
