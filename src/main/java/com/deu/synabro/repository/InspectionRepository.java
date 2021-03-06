package com.deu.synabro.repository;

import com.deu.synabro.entity.Inspection;
import com.deu.synabro.entity.enums.PerformType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

/**
 * 봉사 검수 Repository
 * JpaRepository 를 상속받아 Jpa 메소드를 사용할 수 있다.
 *
 * @author tkfdkskarl56
 * @since 1.0
 */
@Repository
public interface InspectionRepository extends JpaRepository<Inspection, UUID> {
    @Nullable
    Page<Inspection> findByVolunteerWorkId_WorkId_TitleContainingAndPerformTypeOrderByCreatedDateDesc(Pageable pageable, String title, PerformType performType);
    @Nullable
    Page<Inspection> findByVolunteerWorkId_WorkId_TitleContainingOrContentsContainingAndPerformTypeOrderByCreatedDateDesc(Pageable pageable, String title, String content, PerformType performType);
    Page<Inspection> findAllByPerformType(Pageable pageable, PerformType performType);
    Optional<Inspection> findOptionalByIdx(UUID uuid);
}
