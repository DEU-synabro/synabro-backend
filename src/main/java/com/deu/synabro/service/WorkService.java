package com.deu.synabro.service;

import com.deu.synabro.entity.Docs;
import com.deu.synabro.entity.Work;
import com.deu.synabro.http.request.WorkRequest;
import com.deu.synabro.http.request.WorkUpdateRequest;
import com.deu.synabro.http.response.WorkResponse;
import com.deu.synabro.repository.WorkRepository;
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
public class WorkService {
    @Autowired
    WorkRepository workRepository;

    public Work setContent(WorkRequest workRequest, UUID uuid, Docs docs){
        Work work = workRequest.toEntity(uuid);
        work.addDocs(docs);
        return workRepository.save(work);
    }
    public WorkResponse getContentsResponse(Optional<Work> work){
        WorkResponse workResponse = WorkResponse.builder()
                .id(work.get().getIdx())
                .userId(work.get().getUserId().getIdx())
                .title(work.get().getTitle())
                .contents(work.get().getContents())
                .volunteerTime(work.get().getVolunteerTime())
                .createdDate(work.get().getCreatedDate())
                .endedDate(work.get().getEndedDate())
                .updatedDate(work.get().getUpdatedDate())
                .build();
        return workResponse;
    }

    public WorkResponse getNullResponse(){
        WorkResponse workResponse = WorkResponse.builder()
                .id(null)
                .userId(null)
                .title(null)
                .contents(null)
                .volunteerTime(null)
                .createdDate(null)
                .endedDate(null)
                .updatedDate(null)
                .build();
        return workResponse;
    }

    public WorkResponse findByIdAndGetResponse(UUID uuid){
        Optional<Work> work = workRepository.findOptionalByIdx(uuid);
        if(work.isPresent()){
            return getContentsResponse(work);
        }else {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public boolean deleteById(UUID uuid){
        if(workRepository.deleteByIdx(uuid).isEmpty()){
            return false;
        }else {
            return true;
        }
    }

    public Work findById(UUID uuid){
        return workRepository.findByIdx(uuid);
    }

    public Page<Work> findAll(Pageable pageable){
        pageable = PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(),Sort.by("createdDate").descending());
        Page<Work> contentsPage = workRepository.findAll(pageable);
        return contentsPage;
    }
    public Page<Work> findByTitle(Pageable pageable, String title){
        return workRepository.findByTitleContainingOrderByCreatedDateDesc(pageable,title);
    }
    public Page<Work> findByTitleOrContents(Pageable pageable, String title, String contents) {
        return workRepository.findByTitleContainingOrContentsContainingOrderByCreatedDateDesc(pageable,title,contents);
    }

    @Transactional
    public void updateContents(WorkUpdateRequest workUpdateRequest, Work work){
        work.setTitle(workUpdateRequest.getTitle());
        work.setContents(workUpdateRequest.getContents());
        work.setEndedDate(workUpdateRequest.getEndedDate());
        work.setVolunteerTime(workUpdateRequest.getVolunteerTime());
        work.setUpdatedDate(LocalDateTime.now());
    }
}
