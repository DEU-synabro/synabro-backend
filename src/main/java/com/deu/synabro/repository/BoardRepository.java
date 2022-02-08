package com.deu.synabro.repository;

import com.deu.synabro.entity.enums.BoardType;
import com.deu.synabro.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BoardRepository extends JpaRepository<Board,Long> {
    Page<Board> findByTitleContaining(Pageable pageable, String title);
    Page<Board> findByUserId(Pageable pageable, String userid);
    Page<Board> findByTitleContainingOrContentsContaining(Pageable pageable, String title, String content);
    List<Board> deleteByIdAndTitle(UUID id, String title);
    List<Board> findByIdAndTitle(UUID id, String title);
//    List<Board> findByBoardType(@Param("boardType") BoardType boardType);
}
