package com.deu.synabro.repository;

import com.deu.synabro.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BoardRepository extends JpaRepository<Board,Long> {
    Page<Board> findByTitleContainingOrderByCreatedDateDesc(Pageable pageable, String title);
    Page<Board> findByTitleContainingOrContentsContainingOrderByCreatedDateDesc(Pageable pageable, String title, String content);
    List<Board> deleteByIdx(UUID id);
    List<Board> findByIdx(UUID id);
}
