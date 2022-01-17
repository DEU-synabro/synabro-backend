package com.deu.synabro.inspection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface InspectionRepository extends JpaRepository<Inspection, Long> {
}
