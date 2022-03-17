package com.deu.synabro.service;

import com.deu.synabro.entity.Board;
import com.deu.synabro.entity.Member;
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
        Board board = Board.builder()
                            .boardType(boardRequest.getBoardType())
                            .contents(boardRequest.getContents())
                            .title(boardRequest.getTitle())
                            .build();
        return boardRepository.save(board);
    }
    public Page<Board> findAll(Pageable pageable){
        return boardRepository.findAll(pageable);
    }
    public  Page<Board> findByTitle(Pageable pageable,String title){
        return boardRepository.findByTitleContaining(pageable,title);
    }
    public Page<Board> findByTitleOrContents(Pageable pageable,String title, String contents) {
        return boardRepository.findByTitleContainingOrContentsContaining(pageable,title,contents);
    }
    @Transactional
    public boolean deleteById(UUID id){
         if(boardRepository.deleteById(id).isEmpty()){
             return false;
         }else{
             return true;
         }
    }

    public List<Board> findById(UUID id){
        return boardRepository.findById(id);
    }
    @Transactional
    public Board updateBoard(BoardRequest boardRequest, Board boardEntity){
        boardEntity.setTitle(boardRequest.getTitle());
        boardEntity.setContents(boardRequest.getContents());
        boardEntity.setUpdatedDate(LocalDateTime.now());
        return boardEntity;
    }
}

