package com.deu.synabro.repository;

import com.deu.synabro.entity.Member;
import com.deu.synabro.http.response.member.WorkHistoryDetailResponse;
import com.deu.synabro.http.response.member.WorkHistoryResponse;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {
    @EntityGraph(attributePaths = "authorities")
    Optional<Member> findWithAuthoritiesByEmail(String email);
    Optional<Member> findOneWithAuthoritiesByEmail(String email);
    @Query("SELECT new com.deu.synabro.http.response.member.WorkHistoryResponse('VOLUNTEER', volunteer.idx, work.title, work.createdDate) " +
            "FROM VolunteerWork volunteer JOIN volunteer.workId work WHERE volunteer.userId = :member AND LOWER(volunteer.performType) = LOWER('performing')")
    List<WorkHistoryResponse> findVolunteerByIdx(@Param("member") Member member);
    @Query("SELECT new com.deu.synabro.http.response.member.WorkHistoryResponse('INSPECTION', inspection.idx, work.title, work.createdDate) " +
            "FROM Inspection inspection " +
            "LEFT JOIN inspection.volunteerWorkId volunteer " +
            "LEFT JOIN volunteer.workId work " +
            "WHERE inspection.userId = :member AND LOWER(inspection.performType) = LOWER('performing')")
    List<WorkHistoryResponse> findInspectionByIdx(@Param("member") Member member);
    @Query("SELECT new com.deu.synabro.http.response.member.WorkHistoryDetailResponse('VOLUNTEER', volunteer.idx, work.title, work.createdDate, work.contents, volunteer.contents) " +
            "FROM VolunteerWork volunteer JOIN volunteer.workId work WHERE volunteer.userId = :member AND volunteer.idx = :volunteer_id")
    WorkHistoryDetailResponse findVolunteer(@Param("member") Member member, @Param("volunteer_id") UUID id);
    @Query("SELECT new com.deu.synabro.http.response.member.WorkHistoryDetailResponse('INSPECTION', inspection.idx, work.title, work.createdDate, work.contents, inspection.contents) " +
            "FROM Inspection inspection " +
            "LEFT JOIN inspection.volunteerWorkId volunteer " +
            "LEFT JOIN volunteer.workId work " +
            "WHERE inspection.userId = :member AND inspection.idx = :inspection_id")
    WorkHistoryDetailResponse findInspection(@Param("member") Member member, @Param("inspection_id") UUID id);
}