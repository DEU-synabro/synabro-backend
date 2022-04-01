package com.deu.synabro.service;

import com.deu.synabro.entity.Member;
import com.deu.synabro.entity.VolunteerWork;
import com.deu.synabro.http.request.VolunteerWorkUpdateRequest;
import com.deu.synabro.http.response.VolunteerWorkResponse;
import com.deu.synabro.repository.VolunteerWorkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class VolunteerWorkService {

    @Autowired
    VolunteerWorkRepository volunteerWorkRepository;

    public VolunteerWork setVolunteerWork(VolunteerWork volunteerWork){
        return volunteerWorkRepository.save(volunteerWork);
    }

    public VolunteerWorkResponse findByIdAndGetResponse(UUID uuid){
        Optional<VolunteerWork> volunteerWork = volunteerWorkRepository.findOptionalByIdx(uuid);
        if(volunteerWork.isPresent()){
            return getVolunteerResponse(volunteerWork);
        }else {
            throw new IllegalArgumentException();
        }
    }

    public VolunteerWork findById(UUID uuid){
        return volunteerWorkRepository.findByIdx(uuid);
    }

    public  Page<VolunteerWork> findByTitle(Pageable pageable, String title){
        return volunteerWorkRepository.findByWorkId_TitleContainingOrderByCreatedDateDesc(pageable,title);
    }
    public Page<VolunteerWork> findByTitleOrContents(Pageable pageable, String title, String contents) {
        return volunteerWorkRepository.findByWorkId_TitleContainingOrContentsContainingOrderByCreatedDateDesc(pageable,title,contents);
    }
    public Page<VolunteerWork> findAll(Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(), Sort.by("createdDate").descending());
        Page<VolunteerWork> volunteerPage = volunteerWorkRepository.findAll(pageable);
        return volunteerPage;
    }

    @Transactional
    public boolean deleteById(UUID uuid){
        if(volunteerWorkRepository.deleteByIdx(uuid).isEmpty()){
            return false;
        }else{
            return true;
        }
    }

    @Transactional
    public void updateVolunteerWork(VolunteerWorkUpdateRequest volunteerWorkUpdateRequest, VolunteerWork volunteerWork, UUID uuid){
        Member member = new Member(uuid);
        volunteerWork.setUserId(member);
        volunteerWork.setContents(volunteerWorkUpdateRequest.getContents());
        volunteerWork.setUpdatedDate(LocalDateTime.now());
    }

    public VolunteerWorkResponse getVolunteerResponse(Optional<VolunteerWork> volunteerWork){
        VolunteerWorkResponse volunteerWorkResponse = VolunteerWorkResponse.builder()
                    .id(volunteerWork.get().getIdx())
                    .userId(volunteerWork.get().getUserId().getIdx())
                    .workId(volunteerWork.get().getWorkId().getIdx())
                    .workTitle(volunteerWork.get().getWorkId().getTitle())
                    .workContents(volunteerWork.get().getWorkId().getContents())
                    .volunteerWorkContents(volunteerWork.get().getContents())
                    .createdDate(volunteerWork.get().getCreatedDate())
                    .endedDate(volunteerWork.get().getWorkId().getEndedDate())
                    .updatedDate(volunteerWork.get().getUpdatedDate())
                    .build();
        return volunteerWorkResponse;
    }

    public VolunteerWorkResponse getNullResponse(){
        VolunteerWorkResponse volunteerWorkResponse = VolunteerWorkResponse.builder()
                .id(null)
                .userId(null)
                .workId(null)
                .workTitle(null)
                .workContents(null)
                .volunteerWorkContents(null)
                .createdDate(null)
                .endedDate(null)
                .updatedDate(null)
                .build();
        return volunteerWorkResponse;
    }
}
