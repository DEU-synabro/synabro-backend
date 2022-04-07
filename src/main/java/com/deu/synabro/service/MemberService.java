package com.deu.synabro.service;

import com.deu.synabro.entity.Authority;
import com.deu.synabro.entity.Member;
import com.deu.synabro.http.request.SignUpRequest;
import com.deu.synabro.repository.MemberRepository;
import com.deu.synabro.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Member signUp(SignUpRequest signUpRequest) {
        if (memberRepository.findOneWithAuthoritiesByEmail(signUpRequest.getEmail()).orElse(null) != null ){
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER").build();

        Member member = Member.builder()
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .username(signUpRequest.getUsername())
                .authorities(Collections.singleton(authority))
                .build();

        member.setActivated(true);

        return memberRepository.save(member);
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
