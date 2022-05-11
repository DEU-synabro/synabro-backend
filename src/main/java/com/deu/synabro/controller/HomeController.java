package com.deu.synabro.controller;

import com.deu.synabro.service.MemberService;
import com.deu.synabro.service.VolunteerWorkService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.minidev.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name="DashBoard", description = "대시보드 API")
@RestController
@RequestMapping("/api/home")
@AllArgsConstructor
public class HomeController {

    @Autowired
    MemberService memberService;

    @Autowired
    VolunteerWorkService volunteerWorkService;

    @GetMapping("")
    public JSONArray getWeekWork(){
        return volunteerWorkService.getWork(memberService.getMemberWithAuthorities().get().getIdx());
    }
}
