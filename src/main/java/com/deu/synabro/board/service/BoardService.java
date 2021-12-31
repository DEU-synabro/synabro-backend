package com.deu.synabro.board.service;

import com.deu.synabro.board.domain.entity.BoardEntity;
import com.deu.synabro.board.domain.repository.BoardRepository;
import com.deu.synabro.board.dto.BoardDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class BoardService {

    @Autowired
    BoardRepository boardRepository;

    public BoardEntity setBoard(BoardEntity boardEntity){
        return boardRepository.save(boardEntity);
    }

    public List<BoardEntity> getAllBoard(){
        return boardRepository.findAll();
    }

    public List<BoardEntity> findByTitle(String title){
        return boardRepository.findByTitleIsContaining(title);
    }

    public List<BoardEntity> findByWriter(String writer){
        return boardRepository.findByWriterIsContaining(writer);
    }

    @Transactional
    public List<BoardEntity> deleteByTitle(String title){
        return boardRepository.deleteByTitle(title);
    }
}
