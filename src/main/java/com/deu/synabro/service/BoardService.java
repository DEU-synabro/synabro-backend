package com.deu.synabro.service;

import com.deu.synabro.entity.Board;
import com.deu.synabro.repository.BoardRepository;
import com.deu.synabro.http.request.BoardRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
public class BoardService {

    @Autowired
    BoardRepository boardRepository;

    public Board setBoard(BoardRequest boardRequest){
        Board board = boardRequest.toEntity(boardRequest);
        return boardRepository.save(board);
    }

    public Page<Board> findAll(Pageable pageable){
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdDate").descending());
        Page<Board> boardPage = boardRepository.findAll(pageable);
        return boardPage;
    }

    public Page<Board> findByTitle(Pageable pageable,String title){
        return boardRepository.findByTitleContainingOrderByCreatedDateDesc(pageable,title);
    }

    public Page<Board> findByTitleOrContents(Pageable pageable,String title, String contents) {
        return boardRepository.findByTitleContainingOrContentsContainingOrderByCreatedDateDesc(pageable,title,contents);
    }

    @Transactional
    public boolean deleteById(UUID uuid){
//        boardRepository.deleteById(uuid)
        if(boardRepository.deleteByIdx(uuid).isEmpty()){
            return false;
        }else{
            return true;
        }
    }

    public Board findByIdx(UUID uuid){
        Optional<Board> boardOptional = boardRepository.findByIdx(uuid);
        return boardOptional.orElseThrow(() -> new NullPointerException());
    }

    @Transactional
    public void updateBoard(BoardRequest boardRequest, Board boardEntity){
        boardEntity.setTitle(boardRequest.getTitle());
        boardEntity.setContents(boardRequest.getContents());
    }
}

