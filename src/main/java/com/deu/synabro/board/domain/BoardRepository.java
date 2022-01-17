package com.deu.synabro.board.domain;

import com.deu.synabro.board.domain.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@RepositoryRestResource
public interface BoardRepository extends JpaRepository<BoardEntity,Long> {
    List<BoardEntity> findByTitleContaining(String title);
    List<BoardEntity> findByUserid(String userid);
    List<BoardEntity> deleteByTitle(String title);
    List<BoardEntity> findByTitleContainingOrContentsContaining(String title, String content);
    List<BoardEntity> findByUseridAndTitle(String userid, String title);
    List<BoardEntity> findByBoardType(@Param("boardType") BoardType boardType);
}
