package com.deu.synabro.controller;

import com.deu.synabro.http.response.DocsResponse;
import com.deu.synabro.http.response.VideoResponse;
import com.deu.synabro.service.VideoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name="Video", description = "비디오 API")
@RestController
@AllArgsConstructor
public class VideoController {

    @Autowired
    VideoService videoService;

    @PostMapping(value = "upload")
    public ResponseEntity<String> saveFile(MultipartFile file) throws IllegalStateException, IOException {
        System.out.println(file.getOriginalFilename());
        videoService.saveFile(file);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

//    @Operation(tags = "Docs", summary = "비디오를 저장한다",
//            responses={
//                    @ApiResponse(responseCode = "200", description = "비디오 저장 성공",
//                            content = @Content(schema = @Schema(implementation = VideoResponse.class)))
//            })
//    @io.swagger.annotations.ApiResponses(
//            @io.swagger.annotations.ApiResponse(
//                    response = VideoResponse.class, message = "ok", code=200)
//    )
//    @PostMapping(value = "/video")
//    public ResponseEntity<VideoResponse> saveVideo(@RequestPart MultipartFile file,
//                                            @RequestPart(name = "work_id") String workId,
//                                            String url) throws IllegalStateException, IOException{
//        if (url == null){
//            VideoResponse videoResponse = VideoResponse.builder()
//                                                        .workId(workId)
//                                                        .fileName(file.getOriginalFilename())
//                                                        .build();
//            videoService.saveVideo(file,workId);
//            return new ResponseEntity<>(videoResponse, HttpStatus.OK);
//        }else{
//            VideoResponse videoResponse = VideoResponse.builder()
//                    .workId(workId)
//                    .fileName(file.getOriginalFilename())
//                    .build();
//        }
//
//        return new ResponseEntity<>("", HttpStatus.OK);
//    }
}
