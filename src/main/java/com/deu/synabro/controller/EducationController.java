package com.deu.synabro.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Education", description = "교육 데이터 관련 API")
@RequestMapping("/api/educations")
@RestController
public class EducationController {
    @GetMapping
    public void test() {
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
