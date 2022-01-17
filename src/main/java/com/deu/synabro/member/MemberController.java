package com.deu.synabro.member;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import com.deu.synabro.member.domain.Member;
import com.deu.synabro.member.http.request.SignInRequest;
import org.springframework.web.bind.annotation.ResponseBody;

@RepositoryRestController
public class MemberController {
    private MemberRepository memberRepository;

    @Autowired
    public MemberController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @GetMapping(value = "/members/test")
    public @ResponseBody ResponseEntity<?> getMembers() {
        return ResponseEntity.ok("{}");
    }

    @PostMapping(value="/members/signin")
    public @ResponseBody ResponseEntity<?> signIn(@RequestBody SignInRequest signInRequest) {
        Member member = memberRepository.findByUserId(signInRequest.getUserId());
        System.out.println(member);
        if (member != null) {
            if (member.getUserId().equals(signInRequest.getUserId()) &&
                    member.getPassword().equals(signInRequest.getPassword()) ) {
                return ResponseEntity.status(HttpStatus.OK).body("Login Success");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UnAuthorized");
    }
}
