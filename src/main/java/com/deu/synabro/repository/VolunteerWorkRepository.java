package com.deu.synabro.repository;

import com.deu.synabro.entity.VolunteerWork;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VolunteerWorkRepository extends JpaRepository<VolunteerWork, Long> {
    @Nullable
    Page<VolunteerWork> findByWorkId_TitleContainingOrderByCreatedDateDesc(Pageable pageable, String title);
    @Nullable
    Page<VolunteerWork> findByWorkId_TitleContainingOrContentsContainingOrderByCreatedDateDesc(Pageable pageable, String title, String content);
    VolunteerWork findByIdx(UUID uuid);
    Optional<VolunteerWork> findOptionalByIdx(UUID uuid);
    List<VolunteerWork> deleteByIdx(UUID uuid);
}
