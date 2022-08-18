package com.deu.synabro.service;

import com.deu.synabro.entity.*;
import com.deu.synabro.entity.enums.ApprovalType;
import com.deu.synabro.http.request.CertificationRequest;
import com.deu.synabro.http.response.CertificationResponse;
import com.deu.synabro.http.response.TagNameResponse;
import com.deu.synabro.repository.CertificationRepository;
import com.deu.synabro.repository.DocsRepository;
import com.deu.synabro.repository.OffVolunteerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CertificationService {
    @Autowired
    CertificationRepository certificationRepository;

    @Autowired
    OffVolunteerRepository offVolunteerRepository;

    @Autowired
    DocsRepository docsRepository;

    public void setCertification(CertificationRequest certificationRequest, String tagName){
        OffVolunteer offVolunteer = offVolunteerRepository.findByTagName(tagName);
        Certification certification = Certification.builder()
                .offVolunteerId(offVolunteer)
                .userId(new Member(UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName())))
                .title(certificationRequest.getTitle())
                .contents(certificationRequest.getContents())
                .build();
        certificationRepository.save(certification);
    }

    public void setCertificationDocs(CertificationRequest certificationRequest, String tagName, List<Docs> docsList){
        OffVolunteer offVolunteer = offVolunteerRepository.findByTagName(tagName);
        Certification certification = Certification.builder()
                .offVolunteerId(offVolunteer)
                .userId(new Member(UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName())))
                .title(certificationRequest.getTitle())
                .contents(certificationRequest.getContents())
                .build();
        for (Docs docs : docsList) {
            certification.addDocs(docs);
        }
        certificationRepository.save(certification);
    }

    public void setCertificationVideo(CertificationRequest certificationRequest, String tagName, Video video){
        OffVolunteer offVolunteer = offVolunteerRepository.findByTagName(tagName);
        Certification certification = Certification.builder()
                .offVolunteerId(offVolunteer)
                .userId(new Member(UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName())))
                .title(certificationRequest.getTitle())
                .contents(certificationRequest.getContents())
                .build();
        certification.addVideo(video);
        certificationRepository.save(certification);
    }

    @Transactional
    public void deleteById(UUID uuid){ certificationRepository.deleteById(uuid); }

    @Transactional
    public void updateCertification(CertificationRequest certificationRequest, Certification certification){
        certification.setTitle(certificationRequest.getTitle());
        certification.setContents(certificationRequest.getContents());
        certification.setUpdatedDate(LocalDateTime.now());
    }

    public Certification findByIdx(UUID uuid){
        Optional<Certification> certification = certificationRepository.findOptionalByIdx(uuid);
        return certification.orElseThrow(() -> new NullPointerException());
    }

    /**
     * 모든 봉사 요청글을 페이징 처리해주는 메소드입니다.
     *
     * @param pageable 페이징 처리를 위한 Pageable 객체
     * @return 페이징 처리한 봉사 요청글을 반환합니다.
     */
    public Page<Certification> findAll(Pageable pageable){
        pageable = PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(), Sort.by("createdDate").descending());
        return certificationRepository.findAll(pageable);
    }

    /**
     * 입력된 제목과 일치한 봉사 요청글을 페이징 처리를 해주는 메소드입니다.
     *
     * @param pageable 페이징 처리를 위한 Pageable 객체
     * @param title 검색할 제목을 입력합니다.
     * @return
     */
    public Page<Certification> findByTitle(Pageable pageable, String title){
        return certificationRepository.findByTitleContainingOrderByCreatedDateDesc(pageable,title);
    }

    /**
     * 입력한 제목이나 내용과 일치한 봉사 요청글을 페이징 처리를 해주는 메소드입니다.
     *
     * @param pageable 페이징 처리를 해주는 Pageable 객체
     * @param title 검색할 제목을 입력합니다.
     * @param contents 검색할 내용을 입력합니다.
     * @return
     */
    public Page<Certification> findByTitleOrContents(Pageable pageable, String title, String contents) {
        return certificationRepository.findByTitleContainingOrContentsContainingOrderByCreatedDateDesc(pageable,title,contents);
    }

    public CertificationResponse getCertification(Certification certification){
        List<Docs> docs = docsRepository.findByCertification_Idx(certification.getIdx());
        ArrayList<UUID> uuids = new ArrayList<>();
        for(int i=0; i<docs.size(); i++){
            uuids.add(docs.get(i).getIdx());
        }
        CertificationResponse certificationResponse = CertificationResponse.builder()
                .id(certification.getIdx())
                .offVolunteerId(certification.getOffVolunteerId().getIdx())
                .userId(certification.getUserId().getIdx())
                .docsId(uuids)
                .title(certification.getTitle())
                .contents(certification.getContents())
                .createdDate(certification.getCreatedDate())
                .updatedDate(certification.getUpdatedDate())
                .build();
        return certificationResponse;
    }

    public List<TagNameResponse> getTagName(){
        List<OffVolunteer> dataList = offVolunteerRepository.findByApprovalType(ApprovalType.permit);
        List<TagNameResponse> tagNameResponses = new ArrayList<>();
        for(int i=0; i<dataList.size(); i++){
            TagNameResponse tagNameResponse = new TagNameResponse(dataList.get(i).getTagName());
            tagNameResponses.add(tagNameResponse);
        }
        return  tagNameResponses;
    }
}
