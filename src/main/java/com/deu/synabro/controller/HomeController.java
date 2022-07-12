package com.deu.synabro.controller;

import com.deu.synabro.http.response.member.WorkHistoryDetailResponse;
import com.deu.synabro.http.response.member.WorkHistoryListResponse;
import com.deu.synabro.service.MemberService;
import com.deu.synabro.service.VolunteerWorkService;
import com.deu.synabro.service.WorkService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@Tag(name="DashBoard", description = "대시보드 API")
@RestController
@RequestMapping("/api/home")
@AllArgsConstructor
public class HomeController {

    @Autowired
    MemberService memberService;

    @Autowired
    VolunteerWorkService volunteerWorkService;

    @Autowired
    WorkService workService;

    @GetMapping("")
    public JSONObject getWeekWork(){
        return volunteerWorkService.getWork(memberService.getMemberWithAuthorities().get().getIdx());
    }

    @GetMapping("/beneficiary")
    public ResponseEntity<WorkHistoryListResponse> getWork(@PageableDefault Pageable pageable){
        return ResponseEntity.ok(workService.getWorkList(pageable));
    }

    @GetMapping("/beneficiary/{work_id}")
    public ResponseEntity<WorkHistoryDetailResponse> getWorkDetail(@PathVariable(name = "work_id") UUID uuid){
        return ResponseEntity.ok(workService.getWork(uuid));
    }
}
