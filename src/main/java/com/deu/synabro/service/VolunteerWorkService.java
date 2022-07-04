package com.deu.synabro.service;

import com.deu.synabro.entity.Member;
import com.deu.synabro.entity.VolunteerWork;
import com.deu.synabro.entity.Work;
import com.deu.synabro.entity.enums.PerformType;
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

    JSONObject jsonObject = new JSONObject();

    public VolunteerWork setVolunteerWork(Work work, UUID userId){
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
        return volunteerWorkRepository.save(volunteerWork);
    }

    public VolunteerWork findByIdx(UUID uuid){
        Optional<VolunteerWork> volunteerWork = volunteerWorkRepository.findOptionalByIdx(uuid);
        return volunteerWork.orElseThrow(() -> new NullPointerException());
    }

    public  Page<VolunteerWork> findByTitle(Pageable pageable, String title){
        return volunteerWorkRepository.findByWorkId_TitleContainingAndPerformTypeOrderByCreatedDateDesc(pageable,title, PerformType.PERFORMING);
    }
    public Page<VolunteerWork> findByTitleOrContents(Pageable pageable, String title, String contents) {
        return volunteerWorkRepository.findByWorkId_TitleContainingOrContentsContainingAndPerformTypeOrderByCreatedDateDesc(pageable,title,contents, PerformType.PERFORMING);
    }
    public Page<VolunteerWork> findAll(Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(), Sort.by("createdDate").descending());
        Page<VolunteerWork> volunteerPage = volunteerWorkRepository.findAllByPerformType(pageable, PerformType.PERFORMING);
        return volunteerPage;
    }

    @Transactional
    public void deleteById(UUID uuid){
        volunteerWorkRepository.deleteById(uuid);
    }

    @Transactional
    public void updateVolunteerWork(VolunteerWorkUpdateRequest volunteerWorkUpdateRequest, VolunteerWork volunteerWork, UUID uuid){
        volunteerWork.setUserId(new Member(uuid));
        volunteerWork.setContents(volunteerWorkUpdateRequest.getContents());
        volunteerWork.setUpdatedDate(LocalDateTime.now());
    }

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

    public JSONObject getWork(UUID uuid){
        getVolunteerWork(uuid);
        getWeekWork(uuid);
        getMonthWork(uuid);
        return jsonObject;
    }
}
