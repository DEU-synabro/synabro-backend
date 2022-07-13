package com.deu.synabro.service;

import com.deu.synabro.entity.Docs;
import com.deu.synabro.entity.OffVolunteer;
import com.deu.synabro.entity.Video;
import com.deu.synabro.http.request.OffVolunteerUpdateRequest;
import com.deu.synabro.http.response.OffVolunteerResponse;
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

    public void setOffVolunteerDocs(OffVolunteer offVolunteer, UUID uuid, Docs docs){
        OffVolunteer offVolunteers = OffVolunteer.builder()
                .title(offVolunteer.getTitle())
                .contents(offVolunteer.getContents())
                .startPeriod(offVolunteer.getStartPeriod())
                .endPeriod(offVolunteer.getEndPeriod())
                .startDate(offVolunteer.getStartDate())
                .endDate(offVolunteer.getEndDate())
                .build();
        offVolunteers.addDocs(docs);
        offVolunteerRepository.save(offVolunteers);
    }

    public void setOffVolunteerVideo(OffVolunteer offVolunteer, UUID uuid, Video video){
        OffVolunteer offVolunteers = OffVolunteer.builder()
                .title(offVolunteer.getTitle())
                .contents(offVolunteer.getContents())
                .startPeriod(offVolunteer.getStartPeriod())
                .endPeriod(offVolunteer.getEndPeriod())
                .startDate(offVolunteer.getStartDate())
                .endDate(offVolunteer.getEndDate())
                .build();
        offVolunteers.addVideo(video);
        offVolunteerRepository.save(offVolunteers);
    }

    public void setOffVolunteer(OffVolunteer offVolunteer, UUID uuid){
        OffVolunteer offVolunteers = OffVolunteer.builder()
                .title(offVolunteer.getTitle())
                .contents(offVolunteer.getContents())
                .startPeriod(offVolunteer.getStartPeriod())
                .endPeriod(offVolunteer.getEndPeriod())
                .startDate(offVolunteer.getStartDate())
                .endDate(offVolunteer.getEndDate())
                .build();
        offVolunteerRepository.save(offVolunteers);
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
        OffVolunteerResponse offVolunteerResponse = OffVolunteerResponse.builder()
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
