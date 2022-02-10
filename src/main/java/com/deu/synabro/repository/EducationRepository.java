package com.deu.synabro.repository;

import com.deu.synabro.entity.Education;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface EducationRepository extends JpaRepository<Education, Long> {
}
