package com.deu.synabro.controller;


import com.deu.synabro.http.response.GeneralResponse;
import com.deu.synabro.service.ServiceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="Service", description = "봉사 신청서 API")
@RestController
@RequestMapping("/api/service")
@AllArgsConstructor
public class ServiceController {
    @Autowired
    ServiceService serviceService;

//    @PostMapping("")
//    public ResponseEntity<GeneralResponse> createService(@RequestBody ){
//
//    }

}
