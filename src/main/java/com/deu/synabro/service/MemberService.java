package com.deu.synabro.service;

import com.deu.synabro.entity.Authority;
import com.deu.synabro.entity.Member;
import com.deu.synabro.http.request.SignUpRequest;
import com.deu.synabro.http.response.GeneralResponse;
import com.deu.synabro.http.response.member.MemberResponse;
import com.deu.synabro.repository.MemberRepository;
import com.deu.synabro.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Collections;
import java.util.UUID;

@Component
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Page<Member> findAll(Pageable pageable) {
        return memberRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public MemberResponse findMember(Pageable pageable) {
        if (getMemberWithAuthorities().isPresent()) {
            Member member = getMemberWithAuthorities().get();
            return new MemberResponse(pageable);
        } else {
            // TODO 에러 처리 추가
        };
        return null;
    }

    public Member signUp(SignUpRequest signUpRequest, Authority authority) {
        if (memberRepository.findOneWithAuthoritiesByEmail(signUpRequest.getEmail()).orElse(null) != null ){
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        Member member = Member.builder()
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .username(signUpRequest.getUsername())
                .authorities(Collections.singleton(authority))
                .build();

        member.setActivated(true);

        return memberRepository.save(member);
    }

    public boolean checkSignUpRequest(SignUpRequest signUpRequest) {
        if (signUpRequest.getEmail() == null || signUpRequest.getPassword() == null || signUpRequest.getUsername() == null) {
            return false;
        }
        return true;
    }


    @Transactional(readOnly = true)
    public Optional<Member> getMemberWithAuthorities(String email) {
        return memberRepository.findOneWithAuthoritiesByEmail(email);
    }

    @Transactional(readOnly = true)
    public Optional<Member> getMemberWithAuthorities() {
        return SecurityUtil.getCurrentUsername().flatMap(id -> memberRepository.findById(UUID.fromString(id)));
    }
}
