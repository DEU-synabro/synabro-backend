package com.deu.synabro.controller;

import com.deu.synabro.entity.Volunteer;
import com.deu.synabro.http.request.VolunteerRequest;
import com.deu.synabro.http.response.VolunteerResponse;
import com.deu.synabro.service.DocsService;
import com.deu.synabro.service.VideoService;
import com.deu.synabro.service.VolunteerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Tag(name="VolunteerWork", description = "문서 API")
@RestController
@RequestMapping("/api/volunteers")
@AllArgsConstructor
public class VolunteerWorkController {

    @Autowired
    VolunteerService volunteerService;

    @Autowired
    DocsService docsService;

    @Autowired
    VideoService videoService;

    @Operation(tags = "VolunteerWork", summary = "봉사일을 생성합니다.",
            responses={
                    @ApiResponse(responseCode = "200", description = "봉사 생성 성공",
                            content = @Content(schema = @Schema(implementation = VolunteerResponse.class)))
            })
    @io.swagger.annotations.ApiResponses(
            @io.swagger.annotations.ApiResponse(
                    response = VolunteerResponse.class, message = "ok", code=200)
    )
    @PostMapping(value = "")
    public ResponseEntity<Volunteer> saveDocs(
            @Parameter(
                    description = "Files to be uploaded",
                    content = @Content(mediaType = MediaType.ALL_VALUE)  // Won't work without OCTET_STREAM as the mediaType.
            )
            @RequestPart(required = false) MultipartFile file,
                                                       @Parameter(name = "volunteerRequest",content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
                                                       @RequestPart(name = "volunteerRequest") VolunteerRequest volunteerRequest) throws IllegalStateException, IOException {
        Volunteer volunteer = volunteerService.setVolunteerWork(volunteerRequest);

        if (!file.isEmpty()) {
            if(file.getOriginalFilename().contains(".mp4")){
                videoService.saveVideo(file, volunteer);
            }else{
                docsService.saveDocs(file, volunteer);
            }
        }
        return new ResponseEntity<>(volunteer, HttpStatus.OK);
    }
}
