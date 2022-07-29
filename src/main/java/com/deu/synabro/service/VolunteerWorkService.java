package com.deu.synabro.service;

import com.deu.synabro.entity.Member;
import com.deu.synabro.entity.VolunteerWork;
import com.deu.synabro.entity.Work;
import com.deu.synabro.entity.enums.ApprovalType;
import com.deu.synabro.entity.enums.PerformType;
import com.deu.synabro.http.request.VolunteerWorkUpdateRequest;
import com.deu.synabro.http.response.VolunteerListResponse;
import com.deu.synabro.http.response.VolunteerWorkResponse;
import com.deu.synabro.repository.VolunteerWorkRepository;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 봉사 수행글 Service
 * 봉사 수행글 요청에 대한 정보를 가공하여 Controller 에게 데이터를 넘겨준다.
 *
 * @author tkfdkskarl56
 * @since 1.0
 */
@Service
public class VolunteerWorkService {

    @Autowired
    VolunteerWorkRepository volunteerWorkRepository;

    JSONObject jsonObject = new JSONObject();

    /**
     * 봉사 요청글과 수행하는 멤버 uuid 값으로 봉사 수행글을 만들어주는 메소드입니다.
     *
     * @param work 봉사 요청글입니다.
     * @param userId 수행할 멤버 uuid 값입니다.
     * @return
     */
    public void setVolunteerWork(Work work, UUID userId){
        Work workId = Work.builder()
                .idx(work.getIdx())
                .contents(work.getContents())
                .title(work.getTitle())
                .build();

        VolunteerWork volunteerWork = VolunteerWork.builder()
                .userId(new Member(userId))
                .workId(workId)
                .contents("")
                .performType(PerformType.PERFORMING)
                .build();
        volunteerWorkRepository.save(volunteerWork);
    }

    /**
     * uuid 값으로 봉사 수행글을 찾아주는 메소드입니다.
     *
     * @param uuid 봉사 수행글의 uuid 입니다.
     * @return 검색된 봉사 수행글을 반환합니다.
     * @throws NullPointerException 해당 id의 게시글이 없을 경우 예외를 발생시킵니다.
     */
    public VolunteerWork findByIdx(UUID uuid){
        Optional<VolunteerWork> volunteerWork = volunteerWorkRepository.findOptionalByIdx(uuid);
        return volunteerWork.orElseThrow(() -> new NullPointerException());
    }

    /**
     * 입력된 제목과 일치한 봉사 수행글을 페이징 처리를 해주는 메소드입니다.
     *
     * @param pageable 페이징 처리를 위한 Pageable 객체
     * @param title 검색할 제목을 입력합니다.
     * @return 입력한 제목과 일치한 봉사 수행글을 반환합니다.
     */
    public  Page<VolunteerWork> findByTitle(Pageable pageable, String title){
        return volunteerWorkRepository.findByWorkId_TitleContainingAndPerformTypeOrderByCreatedDateDesc(pageable,title, PerformType.PERFORMING);
    }

    /**
     * 입력한 제목이나 내용과 일치한 봉사 수행글을 페이징 처리를 해주는 메소드입니다.
     *
     * @param pageable 페이징 처리를 해주는 Pageable 객체
     * @param title 검색할 제목을 입력합니다.
     * @param contents 검색할 내용을 입력합니다.
     * @return 입력한 제목이나 내용과 일치한 봉사 수행글을 반환합니다.
     */
    public Page<VolunteerWork> findByTitleOrContents(Pageable pageable, String title, String contents) {
        return volunteerWorkRepository.findByWorkId_TitleContainingOrContentsContainingAndPerformTypeOrderByCreatedDateDesc(pageable,title,contents, PerformType.PERFORMING);
    }

    /**
     * 모든 봉사 수행글을 페이징 처리해주는 메소드입니다.
     *
     * @param pageable 페이징 처리를 위한 Pageable 객체
     * @return 페이징 처리한 봉사 수행글을 반환합니다.
     */
    public Page<VolunteerWork> findAll(Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(), Sort.by("createdDate").descending());
        Page<VolunteerWork> volunteerPage = volunteerWorkRepository.findAllByPerformType(pageable, PerformType.PERFORMING);
        return volunteerPage;
    }

    /**
     * 봉사 수행글을 삭제하는 메소드입니다.
     *
     * @param uuid 봉사 수행글의 uuid 입니다.
     */
    @Transactional
    public void deleteById(UUID uuid){
        volunteerWorkRepository.deleteById(uuid);
    }

    /**
     * 요청한 봉사 수행글 정보로 기존 봉사 수행글을 수정해주는 메소드입니다.
     *
     * @param volunteerWorkUpdateRequest 수정할 봉사 수행 내용(내용) 입니다.
     * @param volunteerWork 기존 봉사 수행글
     * @param uuid 수행하는 멤버 uuid
     */
    @Transactional
    public void updateVolunteerWork(VolunteerWorkUpdateRequest volunteerWorkUpdateRequest, VolunteerWork volunteerWork, UUID uuid){
        volunteerWork.setUserId(new Member(uuid));
        volunteerWork.setContents(volunteerWorkUpdateRequest.getContents());
        volunteerWork.setUpdatedDate(LocalDateTime.now());
    }

    /**
     * 봉사 수행글을 Response로 변환시켜 반환해주는 메소드입니다.
     *
     * @param volunteerWork 봉사 수행글 객체
     * @return 봉사 요청글 Response을 반환합니다.
     */
    public VolunteerWorkResponse getVolunteerResponse(VolunteerWork volunteerWork){
        VolunteerWorkResponse volunteerWorkResponse = VolunteerWorkResponse.builder()
                    .id(volunteerWork.getIdx())
                    .userId(volunteerWork.getUserId().getIdx())
                    .workId(volunteerWork.getWorkId().getIdx())
                    .workTitle(volunteerWork.getWorkId().getTitle())
                    .workContents(volunteerWork.getWorkId().getContents())
                    .volunteerWorkContents(volunteerWork.getContents())
                    .createdDate(volunteerWork.getCreatedDate())
                    .endedDate(volunteerWork.getWorkId().getEndedDate())
                    .updatedDate(volunteerWork.getUpdatedDate())
                    .build();
        return volunteerWorkResponse;
    }

    /**
     * 수행중인 봉사 수행글을 가져오는 메소드입니다.
     *
     * @param uuid 멤버 uuid 값입니다.
     * @return 수행중인 봉사 수행글을 JSONObject 값으로 반환합니다.
     */
    public JSONObject getVolunteerWork(UUID uuid){
        List<VolunteerWork> volunteerWorks = volunteerWorkRepository.findByUserId_Idx(uuid);

        JSONArray jsonArray = new JSONArray();
        for(int i=0; i<volunteerWorks.size(); i++){
            JSONObject data = new JSONObject();
            data.put("volunteer_name",volunteerWorks.get(i).getWorkId().getTitle());
            jsonArray.add(data);
        }
        jsonObject.put("work",jsonArray);
        return jsonObject;
    }

    /**
     * 일주일간 봉사 수행글을 가져오는 메소드입니다.
     *
     * @param uuid 멤버 uuid 값입니다.
     * @return 일주일간 봉사 수행글을 JSONObject 값으로 반환합니다.
     */
    public JSONObject getWeekWork(UUID uuid){
        List<VolunteerWork> volunteerWorks = volunteerWorkRepository.findByUserId_Idx(uuid);
        Calendar[] calendars = new Calendar[7];
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        int[] counts = new int[7];

        for(int i=0;i<calendars.length;i++){
            calendars[i]=Calendar.getInstance();
            calendars[i].setTime(new Date());
            calendars[i].add(Calendar.DATE, i);
        }

        for(int i=0; i<volunteerWorks.size(); i++){
            String date=volunteerWorks.get(i).getCreatedDate().toString().substring(0,10);
            for(int z=0; z<calendars.length; z++){
                if(df.format(calendars[z].getTime()).equals(date)){
                    counts[z]+=1;
                }
            }
        }

        JSONArray jsonArray = new JSONArray();
        for(int i=0; i<calendars.length; i++){
            JSONObject data = new JSONObject();
            data.put("date",df.format(calendars[i].getTime()));
            data.put("count",counts[i]);
            jsonArray.add(data);
        }
        jsonObject.put("week",jsonArray);
        return jsonObject;
    }

    /**
     * 한 달간의 봉사 수행글을 가져오는 메소드입니다.
     *
     * @param uuid 멤버 uuid 값입니다.
     * @return 한 달간의 봉사 수행글을 JSONObject 값으로 반환합니다.
     */
    public JSONObject getMonthWork(UUID uuid){
        List<VolunteerWork> volunteerWorks = volunteerWorkRepository.findByUserId_Idx(uuid);
        Calendar calendar = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyy-MM");
        calendar.setTime(new Date());
        JSONArray jsonArray = new JSONArray();

        for(int i=0; i<volunteerWorks.size(); i++){
            String date=volunteerWorks.get(i).getWorkId().getEndedDate().toString().substring(0,7);
            if(df.format(calendar.getTime()).equals(date)){
                JSONObject data = new JSONObject();
                String endDate = volunteerWorks.get(i).getWorkId().getEndedDate().toString().substring(0,10);
                data.put("date",endDate);
                data.put("title",volunteerWorks.get(i).getWorkId().getTitle());
                jsonArray.add(data);
            }
        }
        jsonObject.put("ended_date",jsonArray);
        return jsonObject;
    }

    /**
     * 수행중인 봉사, 한 주의 봉사, 한 달의 봉사수행글을 찾는 메소드들을 실행하는 메소드입니다.
     * @param uuid 멤버 uuid 값입니다.
     * @return JSONObject를 반환합니다.
     */
    public JSONObject getWork(UUID uuid){
        getVolunteerWork(uuid);
        getWeekWork(uuid);
        getMonthWork(uuid);
        return jsonObject;
    }

    /**
     * 페이징 처리할 봉사 수행글을 추가해주는 메소드입니다.
     *
     * @param volunteerWorks 페이징 처리할 봉사 수행글을 입력합니다.
     * @param volunteerListResponseList 페이징 처리된 봉사 요청글을 추가할 리스트를 입력합니다.
     */
    public void addVolunteerListResponse(Page<VolunteerWork> volunteerWorks, List<VolunteerListResponse> volunteerListResponseList){
        if(volunteerWorks.getSize()>=volunteerWorks.getTotalElements()){
            for(int i=0; i<volunteerWorks.getTotalElements(); i++){
                VolunteerListResponse volunteerListResponse = VolunteerListResponse.builder()
                        .idx(volunteerWorks.getContent().get(i).getIdx())
                        .title(volunteerWorks.getContent().get(i).getWorkId().getTitle())
                        .endedDate(volunteerWorks.getContent().get(i).getWorkId().getEndedDate())
                        .build();
                volunteerListResponseList.add(volunteerListResponse);
            }
        }else {
            int contentSize = volunteerWorks.getSize()*volunteerWorks.getNumber();
            for(int i=0; i<volunteerWorks.getSize();i++){
                if(contentSize>=volunteerWorks.getTotalElements())
                    break;
                VolunteerListResponse volunteerListResponse = VolunteerListResponse.builder()
                        .idx(volunteerWorks.getContent().get(i).getIdx())
                        .title(volunteerWorks.getContent().get(i).getWorkId().getTitle())
                        .endedDate(volunteerWorks.getContent().get(i).getWorkId().getEndedDate())
                        .build();
                volunteerListResponseList.add(volunteerListResponse);
                contentSize++;
            }
        }
    }

    public  Page<VolunteerWork> findByTitleAndApproval(Pageable pageable, String title, ApprovalType approvalType){
        return volunteerWorkRepository.findByWorkId_TitleContainingAndApprovalTypeOrderByCreatedDateDesc(pageable,title, approvalType);
    }

    public Page<VolunteerWork> findByTitleOrContentsAndApproval(Pageable pageable, String title, String contents, ApprovalType approvalType) {
        return volunteerWorkRepository.findByWorkId_TitleContainingOrContentsContainingAndApprovalTypeOrderByCreatedDateDesc(pageable,title,contents, approvalType);
    }

    public Page<VolunteerWork> findAllApproval(Pageable pageable, ApprovalType approvalType) {
        return volunteerWorkRepository.findByApprovalTypeOrderByCreatedDateDesc(pageable, approvalType);
    }

    @Transactional
    public void permitVolunteerWork(VolunteerWork volunteerWork){
        volunteerWork.setApprovalType(ApprovalType.permit);
    }

    @Transactional
    public void refuseVolunteerWork(VolunteerWork volunteerWork){
        volunteerWork.setApprovalType(ApprovalType.refuse);
    }
}
