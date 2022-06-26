package com.deu.synabro.service;

import com.deu.synabro.entity.Docs;
import com.deu.synabro.entity.OffVolunteer;
import com.deu.synabro.entity.Work;
import com.deu.synabro.http.request.OffVolunteerUpdateRequest;
import com.deu.synabro.http.response.OffVolunteerResponse;
import com.deu.synabro.http.response.WorkResponse;
import com.deu.synabro.repository.OffVolunteerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
public class OffVolunteerService {

    @Autowired
    OffVolunteerRepository offVolunteerRepository;

    public OffVolunteer setOffVolunteer(OffVolunteer offVolunteer, UUID uuid, Docs docs){
        OffVolunteer offVolunteers = OffVolunteer.builder()
                .title(offVolunteer.getTitle())
                .contents(offVolunteer.getContents())
                .startPeriod(offVolunteer.getStartPeriod())
                .endPeriod(offVolunteer.getEndPeriod())
                .startDate(offVolunteer.getStartDate())
                .endDate(offVolunteer.getEndDate())
                .build();
        offVolunteers.addDocs(docs);
        return offVolunteerRepository.save(offVolunteers);
    }

    public OffVolunteerResponse findByIdAndGetResponse(UUID uuid){
        Optional<OffVolunteer> offVolunteer = offVolunteerRepository.findOptionalByIdx(uuid);
        if(offVolunteer.isPresent()){
            return getOffVolunteerResponse(offVolunteer);
        }else {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public void findByIdAndUpdateOffVolunteer(UUID uuid, OffVolunteerUpdateRequest offVolunteerUpdateRequest){
        Optional<OffVolunteer> offVolunteer = offVolunteerRepository.findOptionalByIdx(uuid);
        if(offVolunteer.isPresent()){
            offVolunteer.get().setTitle(offVolunteerUpdateRequest.getTitle());
            offVolunteer.get().setContents(offVolunteerUpdateRequest.getContents());
            offVolunteer.get().setStartPeriod(offVolunteerUpdateRequest.getStartPeriod());
            offVolunteer.get().setEndPeriod(offVolunteerUpdateRequest.getEndPeriod());
            offVolunteer.get().setStartDate(offVolunteerUpdateRequest.getStartDate());
            offVolunteer.get().setEndDate(offVolunteerUpdateRequest.getEndDate());
        }else {
            throw new IllegalArgumentException();
        }
    }

    public OffVolunteerResponse getOffVolunteerResponse(Optional<OffVolunteer> offVolunteer){
        OffVolunteerResponse res = OffVolunteerResponse.builder()
                .title(offVolunteer.get().getTitle())
                .contents(offVolunteer.get().getContents())
                .startPeriod(offVolunteer.get().getStartPeriod())
                .endPeriod(offVolunteer.get().getEndPeriod())
                .startDate(offVolunteer.get().getStartDate())
                .endDate(offVolunteer.get().getEndDate())
                .createdDate(offVolunteer.get().getCreatedDate())
                .updatedDate(offVolunteer.get().getUpdatedDate())
                .build();
        return res;
    }

    public OffVolunteerResponse getNullResponse(){
        OffVolunteerResponse res = OffVolunteerResponse.builder()
                .title(null)
                .contents(null)
                .startPeriod(null)
                .endPeriod(null)
                .startDate(null)
                .endDate(null)
                .createdDate(null)
                .updatedDate(null)
                .build();
        return res;
    }

    @Transactional
    public boolean deleteById(UUID uuid){
        if(offVolunteerRepository.deleteByIdx(uuid).isEmpty()){
            return false;
        }else {
            return true;
        }
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
