package com.deu.synabro.service;

import com.deu.synabro.entity.Docs;
import com.deu.synabro.entity.OffVolunteer;
import com.deu.synabro.entity.Video;
import com.deu.synabro.entity.enums.ApprovalType;
import com.deu.synabro.http.request.OffVolunteerUpdateRequest;
import com.deu.synabro.http.request.offVolunteer.OffVolunteerRequest;
import com.deu.synabro.http.response.OffVolunteerResponse;
import com.deu.synabro.repository.DocsRepository;
import com.deu.synabro.repository.OffVolunteerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OffVolunteerService {

    @Autowired
    OffVolunteerRepository offVolunteerRepository;

    @Autowired
    DocsRepository docsRepository;

    public void setOffVolunteerDocs(OffVolunteerRequest offVolunteerRequest, List<Docs> docsList){
        OffVolunteer offVolunteers = OffVolunteer.builder()
                .title(offVolunteerRequest.getTitle())
                .contents(offVolunteerRequest.getContents())
                .startPeriod(offVolunteerRequest.getStartPeriod())
                .endPeriod(offVolunteerRequest.getEndPeriod())
                .startDate(offVolunteerRequest.getStartDate())
                .endDate(offVolunteerRequest.getEndDate())
                .tagName(offVolunteerRequest.getTagName())
                .approvalType(ApprovalType.refuse)
                .build();
        for (Docs docs : docsList) {
            offVolunteers.addDocs(docs);
        }
        offVolunteerRepository.save(offVolunteers);
    }

    public void setOffVolunteerVideo(OffVolunteerRequest offVolunteerRequest, Video video){
        OffVolunteer offVolunteers = OffVolunteer.builder()
                .title(offVolunteerRequest.getTitle())
                .contents(offVolunteerRequest.getContents())
                .startPeriod(offVolunteerRequest.getStartPeriod())
                .endPeriod(offVolunteerRequest.getEndPeriod())
                .startDate(offVolunteerRequest.getStartDate())
                .endDate(offVolunteerRequest.getEndDate())
                .build();
        offVolunteers.addVideo(video);
        offVolunteerRepository.save(offVolunteers);
    }

    public void setOffVolunteer(OffVolunteerRequest offVolunteerRequest){
        OffVolunteer offVolunteers = OffVolunteer.builder()
                .title(offVolunteerRequest.getTitle())
                .contents(offVolunteerRequest.getContents())
                .startPeriod(offVolunteerRequest.getStartPeriod())
                .endPeriod(offVolunteerRequest.getEndPeriod())
                .startDate(offVolunteerRequest.getStartDate())
                .endDate(offVolunteerRequest.getEndDate())
                .tagName(offVolunteerRequest.getTagName())
                .approvalType(ApprovalType.refuse)
                .build();
        offVolunteerRepository.save(offVolunteers);
    }

    @Transactional
    public void setCertification(UUID uuid, ApprovalType approvalType){
        OffVolunteer offVolunteer = findByIdx(uuid);
        offVolunteer.setApprovalType(approvalType);
    }

    @Transactional
    public void updateOffVolunteer(OffVolunteerUpdateRequest offVolunteerUpdateRequest, OffVolunteer offVolunteer){
        offVolunteer.setTitle(offVolunteerUpdateRequest.getTitle());
        offVolunteer.setContents(offVolunteerUpdateRequest.getContents());
        offVolunteer.setStartPeriod(offVolunteerUpdateRequest.getStartPeriod());
        offVolunteer.setEndPeriod(offVolunteerUpdateRequest.getEndPeriod());
        offVolunteer.setStartDate(offVolunteerUpdateRequest.getStartDate());
        offVolunteer.setEndDate(offVolunteerUpdateRequest.getEndDate());
    }

    public OffVolunteerResponse getOffVolunteerResponse(OffVolunteer offVolunteer){
        List<Docs> docs = docsRepository.findByOffVolunteer_Idx(offVolunteer.getIdx());
        ArrayList<UUID> uuids = new ArrayList<>();
        for(int i=0; i<docs.size(); i++){
            uuids.add(docs.get(i).getIdx());
        }
        OffVolunteerResponse offVolunteerResponse = OffVolunteerResponse.builder()
                .docsId(uuids)
                .title(offVolunteer.getTitle())
                .contents(offVolunteer.getContents())
                .startPeriod(offVolunteer.getStartPeriod())
                .endPeriod(offVolunteer.getEndPeriod())
                .startDate(offVolunteer.getStartDate())
                .endDate(offVolunteer.getEndDate())
                .createdDate(offVolunteer.getCreatedDate())
                .updatedDate(offVolunteer.getUpdatedDate())
                .build();
        return offVolunteerResponse;
    }

    public OffVolunteer findByIdx(UUID uuid){
        Optional<OffVolunteer> offVolunteer = offVolunteerRepository.findOptionalByIdx(uuid);
        return offVolunteer.orElseThrow(() -> new NullPointerException());
    }

    @Transactional
    public void deleteById(UUID uuid){
        offVolunteerRepository.deleteById(uuid);
    }

    public Page<OffVolunteer> findAll(Pageable pageable){
        pageable = PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(), Sort.by("createdDate").descending());
        Page<OffVolunteer> offVolunteers = offVolunteerRepository.findAll(pageable);
        return offVolunteers;
    }

    public Page<OffVolunteer> findByTitle(Pageable pageable, String title){
        return offVolunteerRepository.findByTitleContainingOrderByCreatedDateDesc(pageable,title);
    }
    public Page<OffVolunteer> findByTitleOrContents(Pageable pageable, String title, String contents) {
        return offVolunteerRepository.findByTitleContainingOrContentsContainingOrderByCreatedDateDesc(pageable,title,contents);
    }
}
