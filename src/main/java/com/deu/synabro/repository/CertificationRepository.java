package com.deu.synabro.repository;

import com.deu.synabro.entity.Certification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CertificationRepository  extends JpaRepository<Certification, UUID> {
    @Nullable
    Page<Certification> findByTitleContainingOrderByCreatedDateDesc(Pageable pageable, String title);
    @Nullable
    Page<Certification> findByTitleContainingOrContentsContainingOrderByCreatedDateDesc(Pageable pageable, String title, String contents);
    Optional<Certification> findOptionalByIdx(UUID uuid);
}
