package com.deu.synabro.controller;

import com.deu.synabro.http.response.education.EducationHomeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
