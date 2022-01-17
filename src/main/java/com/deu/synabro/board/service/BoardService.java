package com.deu.synabro.board.service;

import com.deu.synabro.board.domain.BoardEntity;
import com.deu.synabro.board.domain.BoardRepository;
import com.deu.synabro.board.domain.BoardType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

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
        return boardRepository.findByTitleContaining(title);
    }
    public List<BoardEntity> findByUser_id(String userid){
        return boardRepository.findByUserid(userid);
    }
    public List<BoardEntity> findByTitleOrContents(String title, String contents) {
        return boardRepository.findByTitleContainingOrContentsContaining(title,contents);
    }
    @Transactional
    public List<BoardEntity> deleteByTitle(String title){
        return boardRepository.deleteByTitle(title);
    }

    public List<BoardEntity> findByUser_idAndTitle(String userid, String title){
        return boardRepository.findByUseridAndTitle(userid,title);
    }
    @Transactional
    public BoardEntity UpdateBoard(BoardEntity reqBoard, BoardEntity boardEntity){
        boardEntity.setUserid(reqBoard.getUserid());
        boardEntity.setTitle(reqBoard.getTitle());
        boardEntity.setContents(reqBoard.getContents());
        boardEntity.setUpdated_date(LocalDateTime.now());
        return boardEntity;
    }
}

