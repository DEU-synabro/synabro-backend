package com.deu.synabro.repository;

import com.deu.synabro.entity.Work;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

/**
 * 봉사 요청 Repository
 * JpaRepository 를 상속받아 Jpa 메소드를 사용할 수 있다.
 *
 * @author tkfdkskarl56
 * @since 1.0
 */
@Repository
public interface WorkRepository extends JpaRepository<Work, UUID> {
    @Nullable
    Page<Work> findByTitleContainingOrderByCreatedDateDesc(Pageable pageable, String title);
    @Nullable
    Page<Work> findByTitleContainingOrContentsContainingOrderByCreatedDateDesc(Pageable pageable, String title, String content);
    Optional<Work> findOptionalByIdx(UUID uuid);
}
