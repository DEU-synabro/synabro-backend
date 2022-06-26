package com.deu.synabro.repository;

import com.deu.synabro.entity.OffVolunteer;
import com.deu.synabro.entity.Video;
import com.deu.synabro.entity.Work;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OffVolunteerRepository extends JpaRepository<OffVolunteer, UUID> {
    Optional<OffVolunteer> findOptionalByIdx(UUID uuid);
    List<OffVolunteer> deleteByIdx(UUID uuid);
    OffVolunteer findByIdx(UUID uuid);
    @Nullable
    Page<OffVolunteer> findByTitleContainingOrderByCreatedDateDesc(Pageable pageable, String title);
    @Nullable
    Page<OffVolunteer> findByTitleContainingOrContentsContainingOrderByCreatedDateDesc(Pageable pageable, String title, String content);
}
