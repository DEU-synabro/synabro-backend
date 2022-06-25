package com.deu.synabro.service;

import com.deu.synabro.entity.Docs;
import com.deu.synabro.entity.OffVolunteer;
import com.deu.synabro.repository.OffVolunteerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
