package com.deu.synabro.repository;

import com.deu.synabro.entity.OffVolunteer;
import com.deu.synabro.entity.OffVolunteerApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OffVolunteerApplicationRepository extends JpaRepository<OffVolunteerApplication, UUID> {
}
