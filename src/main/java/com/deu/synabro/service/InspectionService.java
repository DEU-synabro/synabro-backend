package com.deu.synabro.service;

import com.deu.synabro.entity.Inspection;
import com.deu.synabro.entity.Member;
import com.deu.synabro.entity.VolunteerWork;
import com.deu.synabro.entity.enums.PerformType;
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

    public void setInspection(VolunteerWork volunteerWork, UUID uuid){
        volunteerWork.setPerformType(PerformType.DONE);
        Inspection inspection = Inspection.builder()
                                        .volunteerWorkId(volunteerWork)
                                        .contents("")
                                        .userId(new Member(uuid))
                                        .performType(PerformType.PERFORMING)
                                        .build();
        inspectionRepository.save(inspection);
    }

    public Inspection findByIdx(UUID uuid) {
        Optional<Inspection> inspection = inspectionRepository.findOptionalByIdx(uuid);
        return inspection.orElseThrow(() -> new NullPointerException());
    }

    public  Page<Inspection> findByTitle(Pageable pageable, String title){
        return inspectionRepository.findByVolunteerWorkId_WorkId_TitleContainingAndPerformTypeOrderByCreatedDateDesc(pageable,title, PerformType.PERFORMING);
    }
    public Page<Inspection> findByTitleOrContents(Pageable pageable, String title, String contents) {
        return inspectionRepository.findByVolunteerWorkId_WorkId_TitleContainingOrContentsContainingAndPerformTypeOrderByCreatedDateDesc(pageable,title,contents, PerformType.PERFORMING);
    }
    public Page<Inspection> findAll(Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(), Sort.by("createdDate").descending());
        Page<Inspection> inspectionPage = inspectionRepository.findAllByPerformType(pageable, PerformType.PERFORMING);
        return inspectionPage;
    }

    public InspectionResponse getInspectionResponse(Inspection inspection){
        InspectionResponse inspectionResponse = InspectionResponse.builder()
                                                .idx(inspection.getIdx())
                                                .userId(inspection.getUserId().getIdx())
                                                .workId(inspection.getVolunteerWorkId().getWorkId().getIdx())
                                                .volunteerWorkId(inspection.getVolunteerWorkId().getIdx())
                                                .workTitle(inspection.getVolunteerWorkId().getWorkId().getTitle())
                                                .workContents(inspection.getVolunteerWorkId().getWorkId().getContents())
                                                .volunteerWorkContents(inspection.getVolunteerWorkId().getContents())
                                                .inspectionContents(inspection.getContents())
                                                .volunteerTime(inspection.getVolunteerWorkId().getWorkId().getVolunteerTime())
                                                .createdDate(inspection.getCreatedDate())
                                                .endedDate(inspection.getVolunteerWorkId().getWorkId().getEndedDate())
                                                .updatedDate(inspection.getUpdatedDate())
                                                .build();
        if(inspection.getUserId().getIdx()==inspection.getVolunteerWorkId().getUserId().getIdx()){
            inspectionResponse.setUserId(null);
        }
        return inspectionResponse;
    }

    @Transactional
    public void updateInspection(InspectionUpdateRequest inspectionUpdateRequest, Inspection inspection, UUID uuid){
        inspection.setUserId(new Member(uuid));
        inspection.getVolunteerWorkId().getWorkId().setVolunteerTime(inspectionUpdateRequest.getVolunteerTime());
        inspection.setContents(inspectionUpdateRequest.getContents());
        inspection.setUpdatedDate(LocalDateTime.now());
        inspection.setPerformType(PerformType.DONE);
    }

    @Transactional
    public void deleteById(UUID uuid){
        inspectionRepository.deleteById(uuid);
    }
}
