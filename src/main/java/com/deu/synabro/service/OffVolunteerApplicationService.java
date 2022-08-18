package com.deu.synabro.service;

import com.deu.synabro.entity.Member;
import com.deu.synabro.entity.OffVolunteer;
import com.deu.synabro.entity.OffVolunteerApplication;
import com.deu.synabro.entity.enums.ApplyOption;
import com.deu.synabro.http.request.offVolunteer.ApplyBeneficiaryRequest;
import com.deu.synabro.http.request.offVolunteer.BeneficiaryRequest;
import com.deu.synabro.http.response.OffVolunteerApplicationResponse;
import com.deu.synabro.repository.OffVolunteerApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OffVolunteerApplicationService {

    @Autowired
    OffVolunteerApplicationRepository offVolunteerApplicationRepository;

    public void applyOffVolunteer(UUID offVolunteerId){
        if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().isEmpty()){
            throw new RuntimeException("로그인하세요.");
        }
        OffVolunteerApplication offVolunteerApplication = OffVolunteerApplication.builder()
                .offVolunteerId(new OffVolunteer(offVolunteerId))
                .userId(new Member(UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName())))
                .applyOption(ApplyOption.user)
                .build();
        offVolunteerApplicationRepository.save(offVolunteerApplication);
    }

    public void applyOffVolunteerBeneficiary(ApplyBeneficiaryRequest applyBeneficiaryRequest, UUID offVolunteerId){
        if(offVolunteerApplicationRepository.findByNameAndPassword(applyBeneficiaryRequest.getName(),applyBeneficiaryRequest.getPassword()).isPresent()){
            throw new RuntimeException("이미 신청되어 있는 이름과 비밀번호입니다.");
        }
        OffVolunteerApplication offVolunteerApplication = OffVolunteerApplication.builder()
                .offVolunteerId(new OffVolunteer(offVolunteerId))
                .name(applyBeneficiaryRequest.getName())
                .password(applyBeneficiaryRequest.getPassword())
                .phone(applyBeneficiaryRequest.getPhone())
                .teamGroup(applyBeneficiaryRequest.getTeamGroup())
                .address(applyBeneficiaryRequest.getAddress())
                .applyOption(ApplyOption.beneficiary)
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


    public List<OffVolunteerApplicationResponse> getOffVolunteerApplication(UUID uuid) {
        List<OffVolunteerApplication> dataList = offVolunteerApplicationRepository.findByOffVolunteerId_Idx(uuid);
        List<OffVolunteerApplicationResponse> dataResponse = new ArrayList<>();
        for(int i=0; i<dataList.size(); i++){
            OffVolunteerApplicationResponse data = null;
            if(dataList.get(i).getUserId()!=null){
                data = OffVolunteerApplicationResponse.builder()
                        .name(dataList.get(i).getUserId().getUsername())
                        .phone(dataList.get(i).getUserId().getPhone())
                        .teamGroup(dataList.get(i).getUserId().getTeamGroup())
                        .address(dataList.get(i).getUserId().getAddress())
                        .build();
            } else{
                data = OffVolunteerApplicationResponse.builder()
                        .name(dataList.get(i).getName())
                        .phone(dataList.get(i).getPhone())
                        .teamGroup(dataList.get(i).getPhone())
                        .address(dataList.get(i).getAddress())
                        .build();
            }
            dataResponse.add(data);
        }

        return dataResponse;
    }
}
