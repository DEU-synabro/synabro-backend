package com.deu.synabro.docs.controller;

import com.deu.synabro.docs.domain.entity.DocsEntity;
import com.deu.synabro.docs.service.DocsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@RestController
@AllArgsConstructor
@RequestMapping("/docs")
public class DocsController {

    @Autowired
    DocsService docsService;

    @PostMapping(value = "upload")
    public ResponseEntity<String> saveFile(MultipartFile file) throws IllegalStateException, IOException{
        System.out.println(file.getOriginalFilename());
        docsService.saveFile(file);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

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
