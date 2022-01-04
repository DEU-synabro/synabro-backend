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

    @PostMapping("/upload")
    public DocsEntity saveDocs(MultipartFile file, @RequestBody DocsEntity reqEntity) throws IllegalStateException, IOException{
        if( !file.isEmpty() ) {
            file.transferTo(new File(file.getOriginalFilename()));
        }
        return docsService.saveDocs(reqEntity,file.getOriginalFilename());
    }

    @PostMapping(value="uploadFile")
    public ResponseEntity<String> uploadFile(MultipartFile file) throws IllegalStateException, IOException {

        if( !file.isEmpty() ) {
            file.transferTo(new File(file.getOriginalFilename()));
            System.out.println(file.getOriginalFilename());
            System.out.println(file.getResource());
            System.out.println(file.getName());
            System.out.println(file.getContentType());
        }
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
