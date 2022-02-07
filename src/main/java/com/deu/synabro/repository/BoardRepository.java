package com.deu.synabro.repository;

import com.deu.synabro.entity.enums.BoardType;
import com.deu.synabro.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface BoardRepository extends JpaRepository<Board,Long> {
    List<Board> findByTitleContaining(String title);
    List<Board> findByUserid(String userid);
    List<Board> deleteByTitle(String title);
    List<Board> findByTitleContainingOrContentsContaining(String title, String content);
    List<Board> findByUseridAndTitle(String userid, String title);
    List<Board> findByIdAndTitle(Long id, String title);
    List<Board> findByBoardType(@Param("boardType") BoardType boardType);
}
