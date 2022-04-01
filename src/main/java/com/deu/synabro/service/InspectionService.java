package com.deu.synabro.service;

import com.deu.synabro.entity.Inspection;
import com.deu.synabro.entity.Member;
import com.deu.synabro.entity.VolunteerWork;
import com.deu.synabro.http.request.InspectionUpdateRequest;
import com.deu.synabro.http.response.InspectionResponse;
import com.deu.synabro.repository.InspectionRepository;
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
public class InspectionService {
    @Autowired
    InspectionRepository inspectionRepository;

    public void setInspection(VolunteerWork volunteerWork){
        UUID uuid = volunteerWork.getUserId().getIdx();
        Member member = new Member(uuid);
        Inspection inspection = Inspection.builder()
                                        .volunteerWorkId(volunteerWork)
                                        .contents("")
                                        .userId(member)
                                        .build();
        inspectionRepository.save(inspection);
    }

    public InspectionResponse findByIdAndGetResponse(UUID uuid){
        Optional<Inspection> inspection = inspectionRepository.findOptionalByIdx(uuid);
        if(inspection.isPresent()){
            return getInspectionResponse(inspection);
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    public Inspection findById(UUID uuid) {return inspectionRepository.findByIdx(uuid);}

    public  Page<Inspection> findByTitle(Pageable pageable, String title){
        return inspectionRepository.findByVolunteerWorkId_WorkId_TitleContainingOrderByCreatedDateDesc(pageable,title);
    }
    public Page<Inspection> findByTitleOrContents(Pageable pageable, String title, String contents) {
        return inspectionRepository.findByVolunteerWorkId_WorkId_TitleContainingOrContentsContainingOrderByCreatedDateDesc(pageable,title,contents);
    }
    public Page<Inspection> findAll(Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(), Sort.by("createdDate").descending());
        Page<Inspection> inspectionPage = inspectionRepository.findAll(pageable);
        return inspectionPage;
    }

    public InspectionResponse getInspectionResponse(Optional<Inspection> inspection){
        InspectionResponse inspectionResponse = InspectionResponse.builder()
                                                .idx(inspection.get().getIdx())
                                                .userId(inspection.get().getUserId().getIdx())
                                                .workId(inspection.get().getVolunteerWorkId().getWorkId().getIdx())
                                                .volunteerWorkId(inspection.get().getVolunteerWorkId().getIdx())
                                                .workTitle(inspection.get().getVolunteerWorkId().getWorkId().getTitle())
                                                .workContents(inspection.get().getVolunteerWorkId().getWorkId().getContents())
                                                .volunteerWorkContents(inspection.get().getVolunteerWorkId().getContents())
                                                .inspectionContents(inspection.get().getContents())
                                                .createdDate(inspection.get().getCreatedDate())
                                                .endedDate(inspection.get().getVolunteerWorkId().getWorkId().getEndedDate())
                                                .updatedDate(inspection.get().getUpdatedDate())
                                                .build();
        System.out.println(inspection.get().getUserId().getIdx());
        System.out.println(inspection.get().getVolunteerWorkId().getUserId().getIdx());
        if(inspection.get().getUserId().getIdx()==inspection.get().getVolunteerWorkId().getUserId().getIdx()){
            inspectionResponse.setUserId(null);
        }
        return inspectionResponse;
    }

    public InspectionResponse getNullResponse(){
        InspectionResponse inspectionResponse = InspectionResponse.builder()
                .idx(null)
                .userId(null)
                .workId(null)
                .workTitle(null)
                .workContents(null)
                .volunteerWorkContents(null)
                .inspectionContents(null)
                .createdDate(null)
                .endedDate(null)
                .updatedDate(null)
                .build();
        return inspectionResponse;
    }

    @Transactional
    public void updateInspection(InspectionUpdateRequest inspectionUpdateRequest, Inspection inspection, UUID uuid){
        Member member = new Member(uuid);
        inspection.setUserId(member);
        inspection.setContents(inspectionUpdateRequest.getContents());
        inspection.setUpdatedDate(LocalDateTime.now());
    }

    @Transactional
    public boolean deleteById(UUID uuid){
        if(inspectionRepository.deleteByIdx(uuid).isEmpty()){
            return false;
        }else{
            return true;
        }
    }

}
