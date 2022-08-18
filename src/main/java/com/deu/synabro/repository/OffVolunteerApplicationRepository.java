package com.deu.synabro.repository;

import com.deu.synabro.entity.OffVolunteerApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OffVolunteerApplicationRepository extends JpaRepository<OffVolunteerApplication, UUID> {
    Optional<OffVolunteerApplication> findByNameAndPassword(String name, String password);
    List<OffVolunteerApplication> findByOffVolunteerId_Idx(UUID uuid);
}
