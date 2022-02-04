package com.deu.synabro.docs.controller;

import com.deu.synabro.docs.domain.entity.DocsEntity;
import com.deu.synabro.docs.service.DocsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

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

    @Operation(tags = "Docs", summary = "첨부파일을 저장.")
    @PostMapping(value = "upload/docs")
    public ResponseEntity<String> saveDocs(MultipartFile file, String work_id, String contents, String page) throws IllegalStateException, IOException{
        System.out.println(file.getOriginalFilename());
        System.out.println(work_id);
        System.out.println(contents);
        System.out.println(page);
        docsService.saveDocs(file,work_id,contents,page);
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
