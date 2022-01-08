package com.deu.synabro.video.controller;

import com.deu.synabro.video.service.VideoService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("/video")
public class VideoController {

    @Autowired
    VideoService videoService;

    @PostMapping(value = "upload")
    public ResponseEntity<String> saveFile(MultipartFile file) throws IllegalStateException, IOException {
        System.out.println(file.getOriginalFilename());
        videoService.saveFile(file);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @PostMapping(value = "upload/video")
    public ResponseEntity<String> saveVideo(MultipartFile file, String work_id, String contents, String page,String url) throws IllegalStateException, IOException{
        System.out.println(file.getOriginalFilename());
        System.out.println(work_id);
        System.out.println(contents);
        System.out.println(page);
        videoService.saveVideo(file,work_id,contents,page,url);
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
