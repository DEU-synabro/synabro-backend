package com.deu.synabro.repository;

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
public interface WorkRepository extends JpaRepository<Work, UUID> {
    @Nullable
    Page<Work> findByTitleContainingOrderByCreatedDateDesc(Pageable pageable, String title);
    @Nullable
    Page<Work> findByTitleContainingOrContentsContainingOrderByCreatedDateDesc(Pageable pageable, String title, String content);
//    Work findByIdx(UUID uuid);
    Optional<Work> findOptionalByIdx(UUID uuid);
//    Page<Work> findAllByCreatedDate(Pageable pageable);
}
