package com.deu.synabro.controller;


import com.deu.synabro.entity.Docs;
import com.deu.synabro.entity.OffVolunteer;
import com.deu.synabro.entity.Work;
import com.deu.synabro.http.response.GeneralResponse;
import com.deu.synabro.service.DocsService;
import com.deu.synabro.service.OffVolunteerService;
import com.deu.synabro.service.VideoService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Tag(name="offVolunteer", description = "봉사 신청서 API")
@RestController
@RequestMapping("/api/offVolunteer")
@AllArgsConstructor
public class OffVolunteerController {
    @Autowired
    OffVolunteerService offVolunteerService;

    @Autowired
    DocsService docsService;

    @Autowired
    VideoService videoService;

    @PostMapping("")
    public ResponseEntity<GeneralResponse> createOffVolunteer(
        @Parameter(
            description = "Files to be uploaded",
            content = @Content(mediaType = MediaType.ALL_VALUE)  // Won't work without OCTET_STREAM as the mediaType.
        )
        @RequestPart(required = false) MultipartFile file,
        @Parameter(name = "contentsRequest", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
        @RequestPart(name = "contentsRequest") OffVolunteer offVolunteer) throws IOException {
        UUID userId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());

        if (file!=null) {
            if(file.getOriginalFilename().contains(".mp4")){
                videoService.saveVideo(file);
            }else{
                Docs docs = docsService.saveDocs(file);
                offVolunteerService.setOffVolunteer(offVolunteer, userId, docs);
            }
        }
        return new ResponseEntity<>(GeneralResponse.of(HttpStatus.OK,"봉사 요청글이 생성되었습니다."), HttpStatus.OK);
    }

}
