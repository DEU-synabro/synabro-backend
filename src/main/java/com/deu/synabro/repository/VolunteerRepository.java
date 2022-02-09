package com.deu.synabro.repository;

import com.deu.synabro.entity.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {
}
