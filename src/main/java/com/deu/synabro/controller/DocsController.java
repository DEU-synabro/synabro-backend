package com.deu.synabro.controller;

import com.deu.synabro.service.DocsService;
import com.deu.synabro.http.response.DocsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name="Docs", description = "문서 API")
@RestController
@AllArgsConstructor
public class DocsController {

    @Autowired
    DocsService docsService;

//    @Operation(tags = "Docs", summary = "모든 게시판의 글 정보를 반환합니다.")
//    @PostMapping(value = "upload")
//    public ResponseEntity<String> saveFile(MultipartFile file) throws IllegalStateException, IOException{
//        System.out.println(file.getOriginalFilename());
//        docsService.saveFile(file);
//        return new ResponseEntity<>("", HttpStatus.OK);
//    }

    @Operation(tags = "Docs", summary = "첨부파일을 저장한다",
            responses={
                    @ApiResponse(responseCode = "200", description = "첨부파일 저장 성공",
                            content = @Content(schema = @Schema(implementation = DocsResponse.class)))
            })
    @io.swagger.annotations.ApiResponses(
            @io.swagger.annotations.ApiResponse(
                    response = DocsResponse.class, message = "ok", code=200)
    )
    @PostMapping(value = "/docs")
    public ResponseEntity<DocsResponse> saveDocs(@RequestPart MultipartFile file,
                                                 @RequestPart(name = "work_id") String workId,
                                                 @RequestPart String contents,
                                                 @RequestPart String page) throws IllegalStateException, IOException{
        DocsResponse docsResponse = DocsResponse.builder()
                                                .wordId(workId)
                                                .contents(contents)
                                                .page(page)
                                                .fileName(file.getOriginalFilename())
                                                .build();
        docsService.saveDocs(file,workId,contents,page);
        return new ResponseEntity<>(docsResponse, HttpStatus.OK);
    }

}
