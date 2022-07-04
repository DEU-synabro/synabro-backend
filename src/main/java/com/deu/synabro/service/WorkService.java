package com.deu.synabro.service;

import com.deu.synabro.entity.Docs;
import com.deu.synabro.entity.Work;
import com.deu.synabro.http.request.WorkRequest;
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
    public WorkResponse getContentsResponse(Work work){
        WorkResponse workResponse = WorkResponse.builder()
                .id(work.getIdx())
                .userId(work.getUserId().getIdx())
                .title(work.getTitle())
                .contents(work.getContents())
                .volunteerTime(work.getVolunteerTime())
                .createdDate(work.getCreatedDate())
                .endedDate(work.getEndedDate())
                .updatedDate(work.getUpdatedDate())
                .build();
        return workResponse;
    }

    @Transactional
    public void deleteById(UUID uuid){
        workRepository.deleteById(uuid);
    }

    public Work findByIdx(UUID uuid){
        Optional<Work> work = workRepository.findOptionalByIdx(uuid);
        return work.orElseThrow(() -> new NullPointerException());
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
    public void updateContents(WorkRequest workRequest, Work work){
        work.setTitle(workRequest.getTitle());
        work.setContents(workRequest.getContents());
        work.setEndedDate(workRequest.getEndedDate());
        work.setVolunteerTime(workRequest.getVolunteerTime());
        work.setUpdatedDate(LocalDateTime.now());
    }
}
