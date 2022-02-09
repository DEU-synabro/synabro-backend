package com.deu.synabro.service;

import com.deu.synabro.entity.Member;
import com.deu.synabro.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
//
//    @Autowired
//    public MemberService(MemberRepository memberRepository) { this.memberRepository = memberRepository; }

//    public List<Member> findAll() {
//        return memberRepository.findAll();
//    }
//
//    public Page<Member> findAll(Pageable pageable) {
//        return memberRepository.findAll(pageable);
//    }
//
//    public void save(Member member) { memberRepository.save(member); }

}