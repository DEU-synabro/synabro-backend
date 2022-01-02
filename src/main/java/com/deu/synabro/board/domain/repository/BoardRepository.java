package com.deu.synabro.board.domain.repository;

import com.deu.synabro.board.domain.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity,Long> {
    List<BoardEntity> findByTitleContaining(String title);
    List<BoardEntity> findByWriterContaining(String writer);
    List<BoardEntity> deleteByTitle(String title);
    List<BoardEntity> findByTitleContainingOrContentContaining(String title, String content);
    List<BoardEntity> findByWriterAndTitle(String writer, String title);

}
