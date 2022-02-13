package com.deu.synabro.repository;

import com.deu.synabro.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    @EntityGraph(attributePaths = "authorities")
    Optional<Member> findWithAuthoritiesByEmail(String email);

    @Override
    Optional<Member> findById(Long aLong);
}