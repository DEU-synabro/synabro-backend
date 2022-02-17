package com.deu.synabro.service;

import com.deu.synabro.entity.Volunteer;
import com.deu.synabro.http.request.VolunteerRequest;
import com.deu.synabro.repository.VolunteerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

@Service
public class VolunteerService {

    @Autowired
    VolunteerRepository volunteerRepository;

    public Volunteer setVolunteerWork(VolunteerRequest volunteerRequest){
        Volunteer volunteer = volunteerRequest.toEntity();
        return volunteerRepository.save(volunteer);
    }
}
