package com.deu.synabro.docs.controller;

import com.deu.synabro.docs.domain.entity.DocsEntity;
import com.deu.synabro.docs.service.DocsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

}
