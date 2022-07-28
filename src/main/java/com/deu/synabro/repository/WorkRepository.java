package com.deu.synabro.repository;

import com.deu.synabro.entity.Member;
import com.deu.synabro.entity.Work;
import com.deu.synabro.entity.enums.ApprovalType;
import com.deu.synabro.http.response.member.WorkHistoryDetailResponse;
import com.deu.synabro.http.response.member.WorkHistoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.annotation.Nullable;
import java.util.List;
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
    Page<Work> findByApprovalTypeOrderByCreatedDateDesc(Pageable pageable, ApprovalType approvalType);
    @Nullable
    Page<Work> findByTitleContainingAndApprovalTypeOrderByCreatedDateDesc(Pageable pageable, String title, ApprovalType approvalType);
    @Nullable
    Page<Work> findByTitleContainingOrContentsContainingAndApprovalTypeOrderByCreatedDateDesc(Pageable pageable, String title, String content, ApprovalType approvalType);
    Optional<Work> findOptionalByIdx(UUID uuid);
    @Query("SELECT new com.deu.synabro.http.response.member.WorkHistoryResponse('WORK', work.idx, work.title, work.createdDate) " +
            "FROM Work work WHERE work.userId.idx = :uuid")
    List<WorkHistoryResponse> findWorkByIdx(UUID uuid);
    @Query("SELECT new com.deu.synabro.http.response.member.WorkHistoryDetailResponse('Work', work.title, work.createdDate, work.contents, work.contents) " +
            "FROM Work work WHERE work.idx = :uuid")
    WorkHistoryDetailResponse findWork(UUID uuid);
}
