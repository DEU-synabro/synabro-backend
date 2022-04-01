package com.deu.synabro.repository;

import com.deu.synabro.entity.Inspection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InspectionRepository extends JpaRepository<Inspection, Long> {
    @Nullable
    Page<Inspection> findByVolunteerWorkId_WorkId_TitleContainingOrderByCreatedDateDesc(Pageable pageable, String title);
    @Nullable
    Page<Inspection> findByVolunteerWorkId_WorkId_TitleContainingOrContentsContainingOrderByCreatedDateDesc(Pageable pageable, String title, String content);
    Inspection findByIdx(UUID uuid);
    Optional<Inspection> findOptionalByIdx(UUID uuid);
    List<Inspection> deleteByIdx(UUID uuid);

}
