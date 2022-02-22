package com.deu.synabro.service;

import com.deu.synabro.entity.Board;
import com.deu.synabro.repository.BoardRepository;
import com.deu.synabro.http.request.BoardRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class BoardService {

    @Autowired
    BoardRepository boardRepository;

    public Board setBoard(BoardRequest boardRequest){
        Board boardEntity = boardRequest.toEntity();
        return boardRepository.save(boardEntity);
    }
    public Page<Board> findAll(Pageable pageable){
        return boardRepository.findAll(pageable);
    }
    public  Page<Board> findByTitle(Pageable pageable,String title){
        return boardRepository.findByTitleContaining(pageable,title);
    }
    public Page<Board> findByUser_id(Pageable pageable,String userId){
        return boardRepository.findByUserId(pageable,userId);
    }
    public Page<Board> findByTitleOrContents(Pageable pageable,String title, String contents) {
        return boardRepository.findByTitleContainingOrContentsContaining(pageable,title,contents);
    }
    @Transactional
    public List<Board> deleteByTitle(UUID id, String title){
        return boardRepository.deleteByIdAndTitle(id, title);
    }

    public List<Board> findByIdAndTitle(UUID id, String title){
        return boardRepository.findByIdAndTitle(id,title);
    }

    @Transactional
    public Board UpdateBoard(BoardRequest boardRequest, Board boardEntity){
        Board reqBoard = boardRequest.toEntity();
        boardEntity.setUserId(reqBoard.getUserId());
        boardEntity.setTitle(reqBoard.getTitle());
        boardEntity.setContents(reqBoard.getContents());
        boardEntity.setUpdatedDate(LocalDateTime.now());
        return boardEntity;
    }
}

