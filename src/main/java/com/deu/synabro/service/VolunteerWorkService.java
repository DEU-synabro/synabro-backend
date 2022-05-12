package com.deu.synabro.service;

import com.deu.synabro.entity.Member;
import com.deu.synabro.entity.VolunteerWork;
import com.deu.synabro.http.request.VolunteerWorkUpdateRequest;
import com.deu.synabro.http.response.VolunteerWorkResponse;
import com.deu.synabro.repository.VolunteerWorkRepository;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class VolunteerWorkService {

    @Autowired
    VolunteerWorkRepository volunteerWorkRepository;

    public VolunteerWork setVolunteerWork(VolunteerWork volunteerWork){
        return volunteerWorkRepository.save(volunteerWork);
    }

    public VolunteerWorkResponse findByIdAndGetResponse(UUID uuid){
        Optional<VolunteerWork> volunteerWork = volunteerWorkRepository.findOptionalByIdx(uuid);
        if(volunteerWork.isPresent()){
            return getVolunteerResponse(volunteerWork);
        }else {
            throw new IllegalArgumentException();
        }
    }

    public VolunteerWork findById(UUID uuid){
        return volunteerWorkRepository.findByIdx(uuid);
    }

    public  Page<VolunteerWork> findByTitle(Pageable pageable, String title){
        return volunteerWorkRepository.findByWorkId_TitleContainingOrderByCreatedDateDesc(pageable,title);
    }
    public Page<VolunteerWork> findByTitleOrContents(Pageable pageable, String title, String contents) {
        return volunteerWorkRepository.findByWorkId_TitleContainingOrContentsContainingOrderByCreatedDateDesc(pageable,title,contents);
    }
    public Page<VolunteerWork> findAll(Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(), Sort.by("createdDate").descending());
        Page<VolunteerWork> volunteerPage = volunteerWorkRepository.findAll(pageable);
        return volunteerPage;
    }

    @Transactional
    public boolean deleteById(UUID uuid){
        if(volunteerWorkRepository.deleteByIdx(uuid).isEmpty()){
            return false;
        }else{
            return true;
        }
    }

    @Transactional
    public void updateVolunteerWork(VolunteerWorkUpdateRequest volunteerWorkUpdateRequest, VolunteerWork volunteerWork, UUID uuid){
        Member member = new Member(uuid);
        volunteerWork.setUserId(member);
        volunteerWork.setContents(volunteerWorkUpdateRequest.getContents());
        volunteerWork.setUpdatedDate(LocalDateTime.now());
    }

    public VolunteerWorkResponse getVolunteerResponse(Optional<VolunteerWork> volunteerWork){
        VolunteerWorkResponse volunteerWorkResponse = VolunteerWorkResponse.builder()
                    .id(volunteerWork.get().getIdx())
                    .userId(volunteerWork.get().getUserId().getIdx())
                    .workId(volunteerWork.get().getWorkId().getIdx())
                    .workTitle(volunteerWork.get().getWorkId().getTitle())
                    .workContents(volunteerWork.get().getWorkId().getContents())
                    .volunteerWorkContents(volunteerWork.get().getContents())
                    .createdDate(volunteerWork.get().getCreatedDate())
                    .endedDate(volunteerWork.get().getWorkId().getEndedDate())
                    .updatedDate(volunteerWork.get().getUpdatedDate())
                    .build();
        return volunteerWorkResponse;
    }

    public VolunteerWorkResponse getNullResponse(){
        VolunteerWorkResponse volunteerWorkResponse = VolunteerWorkResponse.builder()
                .id(null)
                .userId(null)
                .workId(null)
                .workTitle(null)
                .workContents(null)
                .volunteerWorkContents(null)
                .createdDate(null)
                .endedDate(null)
                .updatedDate(null)
                .build();
        return volunteerWorkResponse;
    }

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

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for(int i=0; i<calendars.length; i++){
            JSONObject data = new JSONObject();
            data.put("날짜",df.format(calendars[i].getTime()));
            data.put("봉사 개수",counts[i]);
            jsonArray.add(data);
        }
        jsonObject.put("일주일 작업 현황",jsonArray);
        return jsonObject;
    }
    public JSONObject getMonthWork(UUID uuid){
        List<VolunteerWork> volunteerWorks = volunteerWorkRepository.findByUserId_Idx(uuid);
        Calendar calendar = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyy-MM");
        calendar.setTime(new Date());
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        for(int i=0; i<volunteerWorks.size(); i++){
            String date=volunteerWorks.get(i).getWorkId().getEndedDate().toString().substring(0,7);
            if(df.format(calendar.getTime()).equals(date)){
                JSONObject data = new JSONObject();
                String endDate = volunteerWorks.get(i).getWorkId().getEndedDate().toString().substring(0,10);
                data.put("마감 날짜",endDate);
                data.put("작업 제목",volunteerWorks.get(i).getWorkId().getTitle());
                jsonArray.add(data);
            }
        }
        jsonObject.put("작업 마감 일정",jsonArray);
        return jsonObject;
    }

    public JSONArray getWork(UUID uuid){
        JSONArray jsonArray = new JSONArray();
        JSONObject weekjson = getWeekWork(uuid);
        JSONObject monthjson = getMonthWork(uuid);
        jsonArray.add(weekjson);
        jsonArray.add(monthjson);
        return jsonArray;
    }
}
