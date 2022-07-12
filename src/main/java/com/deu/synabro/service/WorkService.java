package com.deu.synabro.service;

import com.deu.synabro.entity.Docs;
import com.deu.synabro.entity.Member;
import com.deu.synabro.entity.Work;
import com.deu.synabro.http.request.WorkRequest;
import com.deu.synabro.http.response.WorkResponse;
import com.deu.synabro.http.response.member.WorkHistoryDetailResponse;
import com.deu.synabro.http.response.member.WorkHistoryListResponse;
import com.deu.synabro.http.response.member.WorkHistoryResponse;
import com.deu.synabro.repository.WorkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 봉사 요청글 Service
 * 봉사 요청글 요청에 대한 정보를 가공하여 Controller 에게 데이터를 넘겨준다.
 *
 * @author tkfdkskarl56
 * @since 1.0
 */
@Service
public class WorkService {
    @Autowired
    WorkRepository workRepository;

    @Autowired
    MemberService memberService;
    /**
     * 요청한 봉사 요청글 정보로 봉사 요청글을 생성해주는 메소드입니다.
     *
     * @param workRequest 봉사 요청 내용(제목, 내용, 봉사 시간, 마감일) 입니다.
     * @param uuid 봉사 요청하는 사람의 uuid 입니다
     * @param docs 저장할 사진입니다.
     */
    public void setContent(WorkRequest workRequest, UUID uuid, Docs docs){
        Work work = workRequest.toEntity(uuid);
        work.addDocs(docs);
        workRepository.save(work);
    }

    /**
     * 봉사 요청글을 Response로 변환시켜 반환해주는 메소드입니다.
     *
     * @param work 봉사 요청글 객체
     * @return 봉사 요청글 Response을 반환합니다.
     */
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

    /**
     * 봉사 요청글을 삭제하는 메소드입니다.
     *
     * @param uuid 봉사 요청글의 uuid 입니다.
     */
    @Transactional
    public void deleteById(UUID uuid){
        workRepository.deleteById(uuid);
    }

    /**
     * uuid 값으로 봉사 요청글을 찾아주는 메소드입니다.
     *
     * @param uuid 봉사 요청글의 uuid 입니다.
     * @return 검색된 봉사 요청글을 반환합니다.
     * @throws NullPointerException 해당 id의 게시글이 없을 경우 예외를 발생시킵니다.
     */
    public Work findByIdx(UUID uuid){
        Optional<Work> work = workRepository.findOptionalByIdx(uuid);
        return work.orElseThrow(() -> new NullPointerException());
    }

    /**
     * 모든 봉사 요청글을 페이징 처리해주는 메소드입니다.
     *
     * @param pageable 페이징 처리를 위한 Pageable 객체
     * @return 페이징 처리한 봉사 요청글을 반환합니다.
     */
    public Page<Work> findAll(Pageable pageable){
        pageable = PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(),Sort.by("createdDate").descending());
        Page<Work> contentsPage = workRepository.findAll(pageable);
        return contentsPage;
    }

    /**
     * 입력된 제목과 일치한 봉사 요청글을 페이징 처리를 해주는 메소드입니다.
     *
     * @param pageable 페이징 처리를 위한 Pageable 객체
     * @param title 검색할 제목을 입력합니다.
     * @return
     */
    public Page<Work> findByTitle(Pageable pageable, String title){
        return workRepository.findByTitleContainingOrderByCreatedDateDesc(pageable,title);
    }

    /**
     * 입력한 제목이나 내용과 일치한 봉사 요청글을 페이징 처리를 해주는 메소드입니다.
     *
     * @param pageable 페이징 처리를 해주는 Pageable 객체
     * @param title 검색할 제목을 입력합니다.
     * @param contents 검색할 내용을 입력합니다.
     * @return
     */
    public Page<Work> findByTitleOrContents(Pageable pageable, String title, String contents) {
        return workRepository.findByTitleContainingOrContentsContainingOrderByCreatedDateDesc(pageable,title,contents);
    }

    /**
     * 요청한 봉사 요청글 정보로 기존 봉사 요청글을 수정해주는 메소드입니다.
     *
     * @param workRequest 수정할 봉사 요청 내용(제목, 내용, 봉사 시간, 마감일) 입니다.
     * @param work 기존 봉사 요청글
     */
    @Transactional
    public void updateContents(WorkRequest workRequest, Work work){
        work.setTitle(workRequest.getTitle());
        work.setContents(workRequest.getContents());
        work.setEndedDate(workRequest.getEndedDate());
        work.setVolunteerTime(workRequest.getVolunteerTime());
        work.setUpdatedDate(LocalDateTime.now());
    }

    @Transactional
    public WorkHistoryListResponse getWorkList(Pageable pageable){
        if(memberService.getMemberWithAuthorities().isPresent()){
            List<WorkHistoryResponse> data = workRepository.findWorkByIdx(memberService.getMemberWithAuthorities().get().getIdx());
            return new WorkHistoryListResponse(data, pageable);
        }
        return null;
    }

    public WorkHistoryDetailResponse getWork(UUID uuid){
        List<WorkHistoryResponse> data = workRepository.findWorkByIdx(memberService.getMemberWithAuthorities().get().getIdx());
        WorkHistoryResponse work = data.stream().filter(value -> uuid.equals(value.getId())).findAny().orElse(null);
        Integer index = data.indexOf(work);
        System.out.println(data.size());
        System.out.println(index);
        WorkHistoryDetailResponse response = workRepository.findWork(uuid);
        if(index-1 >= 0) {
            response.setBeforeWork(data.get(index - 1));
        }
        if(index+1 < data.size()) {
            response.setAfterWork(data.get(index+1));
        }

        return response;
    }
}
