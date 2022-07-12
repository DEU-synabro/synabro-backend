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

/**
 * 봉사 검수글 Service
 * 봉사 검수글 요청에 대한 정보를 가공하여 Controller 에게 데이터를 넘겨준다.
 *
 * @author tkfdkskarl56
 * @since 1.0
 */
@Service
public class InspectionService {
    @Autowired
    InspectionRepository inspectionRepository;

    /**
     * 봉사 검수글과 검수하는 멤버 uuid 값으로 봉사 검수글을 만들어주는 메소드입니다.
     * @param volunteerWork 봉사 수행글입니다.
     * @param uuid 검수할 멤버 uuid 갑입니다.
     */
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

    /**
     * uuid 값으로 봉사 검수글을 찾아주는 메소드입니다.
     *
     * @param uuid 봉사 검수글의 uuid 입니다.
     * @return 검색된 봉사 검수글을 반환합니다.
     * @throws NullPointerException 해당 id의 게시글이 없을 경우 예외를 발생시킵니다.
     */
    public Inspection findByIdx(UUID uuid) {
        Optional<Inspection> inspection = inspectionRepository.findOptionalByIdx(uuid);
        return inspection.orElseThrow(() -> new NullPointerException());
    }

    /**
     * 입력된 제목과 일치한 봉사 검수글을 페이징 처리를 해주는 메소드입니다.
     *
     * @param pageable 페이징 처리를 위한 Pageable 객체
     * @param title 검색할 제목을 입력합니다.
     * @return 입력한 제목과 일치한 봉사 검수글을 반환합니다.
     */
    public  Page<Inspection> findByTitle(Pageable pageable, String title){
        return inspectionRepository.findByVolunteerWorkId_WorkId_TitleContainingAndPerformTypeOrderByCreatedDateDesc(pageable,title, PerformType.PERFORMING);
    }

    /**
     * 입력한 제목이나 내용과 일치한 봉사 검수글을 페이징 처리를 해주는 메소드입니다.
     *
     * @param pageable 페이징 처리를 해주는 Pageable 객체
     * @param title 검색할 제목을 입력합니다.
     * @param contents 검색할 내용을 입력합니다.
     * @return 입력한 제목이나 내용과 일치한 봉사 검수글을 반환합니다.
     */
    public Page<Inspection> findByTitleOrContents(Pageable pageable, String title, String contents) {
        return inspectionRepository.findByVolunteerWorkId_WorkId_TitleContainingOrContentsContainingAndPerformTypeOrderByCreatedDateDesc(pageable,title,contents, PerformType.PERFORMING);
    }

    /**
     * 모든 봉사 검수글을 페이징 처리해주는 메소드입니다.
     *
     * @param pageable 페이징 처리를 위한 Pageable 객체
     * @return 페이징 처리한 봉사 검수글을 반환합니다.
     */
    public Page<Inspection> findAll(Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(), Sort.by("createdDate").descending());
        Page<Inspection> inspectionPage = inspectionRepository.findAllByPerformType(pageable, PerformType.PERFORMING);
        return inspectionPage;
    }

    /**
     * 봉사 검수글을 Response로 변환시켜 반환해주는 메소드입니다.
     *
     * @param inspection 봉사 검수글 객체
     * @return 봉사 요청글 Response 을 반환합니다.
     */
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

    /**
     * 요청한 봉사 검수글 정보로 기존 봉사 검수글을 수정해주는 메소드입니다.
     *
     * @param inspectionUpdateRequest 수정할 봉사 검수 내용(봉사 시간, 내용) 입니다.
     * @param inspection 기존 봉사 검수글
     * @param uuid 검수하는 멤버 uuid
     */
    @Transactional
    public void updateInspection(InspectionUpdateRequest inspectionUpdateRequest, Inspection inspection, UUID uuid){
        inspection.setUserId(new Member(uuid));
        inspection.getVolunteerWorkId().getWorkId().setVolunteerTime(inspectionUpdateRequest.getVolunteerTime());
        inspection.setContents(inspectionUpdateRequest.getContents());
        inspection.setUpdatedDate(LocalDateTime.now());
        inspection.setPerformType(PerformType.DONE);
    }

    /**
     * 봉사 검수글을 삭제하는 메소드입니다.
     *
     * @param uuid 봉사 검수글의 uuid 입니다.
     */
    @Transactional
    public void deleteById(UUID uuid){
        inspectionRepository.deleteById(uuid);
    }
}
