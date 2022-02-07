package com.deu.synabro.service;

import com.deu.synabro.entity.Board;
import com.deu.synabro.repository.BoardRepository;
import com.deu.synabro.http.request.BoardRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BoardService {

    @Autowired
    BoardRepository boardRepository;

    public Board setBoard(BoardRequest boardRequest){
        Board boardEntity = boardRequest.toEntity();
        return boardRepository.save(boardEntity);
    }
    public List<Board> getAllBoard(){
        return boardRepository.findAll();
    }
    public List<Board> findByTitle(String title){
        return boardRepository.findByTitleContaining(title);
    }
    public List<Board> findByUser_id(String userid){
        return boardRepository.findByUserid(userid);
    }
    public List<Board> findByTitleOrContents(String title, String contents) {
        return boardRepository.findByTitleContainingOrContentsContaining(title,contents);
    }
    @Transactional
    public List<Board> deleteByTitle(String title){
        return boardRepository.deleteByTitle(title);
    }

    public List<Board> findByUser_idAndTitle(String userid, String title){
        return boardRepository.findByUseridAndTitle(userid,title);
    }

    public List<Board> findByIdAndTitle(Long id, String title){
        return boardRepository.findByIdAndTitle(id,title);
    }

    @Transactional
    public Board UpdateBoard(BoardRequest boardRequest, Board boardEntity){
        Board reqBoard = boardRequest.toEntity();
        boardEntity.setUserid(reqBoard.getUserid());
        boardEntity.setTitle(reqBoard.getTitle());
        boardEntity.setContents(reqBoard.getContents());
        boardEntity.setModifiedDate(LocalDateTime.now());
        return boardEntity;
    }
}

