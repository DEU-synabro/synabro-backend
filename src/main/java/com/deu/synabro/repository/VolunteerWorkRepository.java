package com.deu.synabro.repository;

import com.deu.synabro.entity.VolunteerWork;
import com.deu.synabro.entity.enums.PerformType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 봉사 수행 Repository
 * JpaRepository 를 상속받아 Jpa 메소드를 사용할 수 있다.
 *
 * @author tkfdkskarl56
 * @since 1.0
 */
@Repository
public interface VolunteerWorkRepository extends JpaRepository<VolunteerWork, UUID> {
    @Nullable
    Page<VolunteerWork> findByWorkId_TitleContainingAndPerformTypeOrderByCreatedDateDesc(Pageable pageable, String title, PerformType performType);
    @Nullable
    Page<VolunteerWork> findByWorkId_TitleContainingOrContentsContainingAndPerformTypeOrderByCreatedDateDesc(Pageable pageable, String title, String content, PerformType performType);
    Page<VolunteerWork> findAllByPerformType(Pageable pageable, PerformType performType);
    VolunteerWork findByIdx(UUID uuid);
    Optional<VolunteerWork> findOptionalByIdx(UUID uuid);
    List<VolunteerWork> findByUserId_Idx(UUID uuid);
}
