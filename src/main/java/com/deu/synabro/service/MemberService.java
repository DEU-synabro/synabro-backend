package com.deu.synabro.service;

import com.deu.synabro.entity.Authority;
import com.deu.synabro.entity.Member;
import com.deu.synabro.entity.enums.PerformType;
import com.deu.synabro.http.request.member.MemberPatchRequest;
import com.deu.synabro.http.request.member.SignUpRequest;
import com.deu.synabro.http.response.member.MemberResponse;
import com.deu.synabro.http.response.member.WorkHistoryDetailResponse;
import com.deu.synabro.http.response.member.WorkHistoryListResponse;
import com.deu.synabro.http.response.member.WorkHistoryResponse;
import com.deu.synabro.repository.MemberRepository;
import com.deu.synabro.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.Collections;
import java.util.UUID;
import java.util.List;

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
            List<WorkHistoryResponse> data = memberRepository.findVolunteerByIdx(getMemberWithAuthorities().get());
            data.addAll(memberRepository.findInspectionByIdx(getMemberWithAuthorities().get()));
            return new MemberResponse(getMemberWithAuthorities().get(), data, pageable);
        } else {
            // TODO 에러 처리 추가
        };
        return null;
    }

    @Transactional
    public WorkHistoryListResponse findWorkList(Pageable pageable) {
        if (getMemberWithAuthorities().isPresent()) {
            List<WorkHistoryResponse> data = memberRepository.findVolunteerByIdx(getMemberWithAuthorities().get());
            data.addAll(memberRepository.findInspectionByIdx(getMemberWithAuthorities().get()));
            return new WorkHistoryListResponse(data, pageable);
        } else {
            // TODO 에러 처리 추가
        };
        return null;
    }

    @Transactional
    public WorkHistoryDetailResponse findVolunteerWork(UUID id) {
        List<WorkHistoryResponse> data = memberRepository.findVolunteerByIdx(getMemberWithAuthorities().get());
        data.addAll(memberRepository.findInspectionByIdx(getMemberWithAuthorities().get()));

        WorkHistoryResponse work = data.stream().filter(value -> id.equals(value.getId())).findAny().orElse(null);
        Integer index = data.indexOf(work);
        WorkHistoryDetailResponse response = memberRepository.findVolunteer(getMemberWithAuthorities().get(), id);
        if(index-1 >= 0) {
            response.setBeforeWork(data.get(index - 1));
        }
        if(index+1 <= data.size()) {
            response.setAfterWork(data.get(index+1));
        }

        return response;
    }

    @Transactional
    public WorkHistoryDetailResponse findInspectionWork(UUID id) {
        List<WorkHistoryResponse> data = memberRepository.findVolunteerByIdx(getMemberWithAuthorities().get());
        data.addAll(memberRepository.findInspectionByIdx(getMemberWithAuthorities().get()));

        WorkHistoryResponse work = data.stream().filter(value -> id.equals(value.getId())).findAny().orElse(null);
        Integer index = data.indexOf(work);
        WorkHistoryDetailResponse response = memberRepository.findInspection(getMemberWithAuthorities().get(), id);
        if(index-1 >= 0) {
            response.setBeforeWork(data.get(index - 1));
        }
        if(index+1 < data.size()) {
            response.setAfterWork(data.get(index+1));
        }

        return response;
    }

    public Member update(MemberPatchRequest request) {
        Member member = getMemberWithAuthorities().get();
        return memberRepository.save(member.update(request));
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
        return signUpRequest.getEmail() == null || signUpRequest.getPassword() == null || signUpRequest.getUsername() == null;
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
