package com.deu.synabro.repository;

import com.deu.synabro.entity.Board;
import com.deu.synabro.entity.enums.BoardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 게시글 Repository
 * JpaRepository 를 상속받아 Jpa 메소드를 사용할 수 있다.
 */
@Repository
public interface BoardRepository extends JpaRepository<Board, UUID> {
    Page<Board> findByBoardType(Pageable pageable, BoardType boardType);
    Page<Board> findByTitleContainingAndBoardType(Pageable pageable, String title, BoardType boardType);
    Page<Board> findByTitleContainingOrContentsContainingAndBoardType(Pageable pageable, String title, String content, BoardType boardType);
    List<Board> findBoardByBoardType(BoardType boardType);
    Optional<Board> findByIdx(UUID uuid);
}
