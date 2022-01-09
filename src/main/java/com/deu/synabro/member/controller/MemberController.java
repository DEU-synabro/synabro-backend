package com.deu.synabro.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("members")
public class MemberController {
    @GetMapping("/signin")
    public String signin() {
        return "signin test";
    }
}
