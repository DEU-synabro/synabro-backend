package com.deu.synabro.service;

import com.deu.synabro.entity.Member;
import com.deu.synabro.entity.OffVolunteer;
import com.deu.synabro.entity.OffVolunteerApplication;
import com.deu.synabro.entity.enums.ApplyOption;
import com.deu.synabro.http.request.offVolunteer.ApplyBeneficiaryRequest;
import com.deu.synabro.http.request.offVolunteer.BeneficiaryRequest;
import com.deu.synabro.repository.OffVolunteerApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
public class OffVolunteerApplicationService {

    @Autowired
    OffVolunteerApplicationRepository offVolunteerApplicationRepository;

    public void applyOffVolunteer(UUID offVolunteerId){
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().isEmpty()){
            throw new RuntimeException("로그인하세요.");
        }
        OffVolunteerApplication offVolunteerApplication = OffVolunteerApplication.builder()
                .offVolunteerId(new OffVolunteer(offVolunteerId))
                .userId(new Member(UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName())))
                .applyOption(ApplyOption.member)
                .build();
        offVolunteerApplicationRepository.save(offVolunteerApplication);
    }

    public void applyOffVolunteerBeneficiary(ApplyBeneficiaryRequest applyBeneficiaryRequest){
        if(offVolunteerApplicationRepository.findByNameAndPassword(applyBeneficiaryRequest.getName(),applyBeneficiaryRequest.getPassword()).isPresent()){
            throw new RuntimeException("이미 신청되어 있는 이름과 비밀번호입니다.");
        }
        OffVolunteerApplication offVolunteerApplication = OffVolunteerApplication.builder()
                .name(applyBeneficiaryRequest.getName())
                .password(applyBeneficiaryRequest.getPassword())
                .phone(applyBeneficiaryRequest.getPhone())
                .teamGroup(applyBeneficiaryRequest.getTeamGroup())
                .applyOption(ApplyOption.non_member)
                .build();
        offVolunteerApplicationRepository.save(offVolunteerApplication);
    }

    @Transactional
    public void cancelOffVolunteer(UUID uuid){
        offVolunteerApplicationRepository.deleteById(uuid);
    }

    @Transactional
    public void cancelOffVolunteerBeneficiary(BeneficiaryRequest beneficiaryRequest){
        Optional<OffVolunteerApplication> offVolunteerApplication = offVolunteerApplicationRepository.findByNameAndPassword(beneficiaryRequest.getName(), beneficiaryRequest.getPassword());
        offVolunteerApplicationRepository.deleteById(offVolunteerApplication.get().getIdx());
    }


}
