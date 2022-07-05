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

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

/**
 * 게시글 Service
 * 게시글 요청에 대한 정보를 가공하여 Controller 에게 데이터를 넘겨준다.
 */
@Service
public class BoardService {

    @Autowired
    BoardRepository boardRepository;

    /**
     * 요청한 게시글 정보로 게시글을 생성해주는 메소드입니다.
     * @param boardRequest 게시글 정보 클래스(제목, 내용, 종류)를 입력합니다.
     * @return 게시글 생성을 반환합니다.
     */
    public Board setBoard(BoardRequest boardRequest){
        Board board = boardRequest.toEntity(boardRequest);
        return boardRepository.save(board);
    }

    /**
     * 모든 게시글을 페이징 처리해주는 메소드입니다.
     * @param pageable 페이징 처리를 위한 Pageable 객체
     * @return 페이징 처리한 게시글을 반환합니다.
     */
    public Page<Board> findAll(Pageable pageable){
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdDate").descending());
        return boardRepository.findAll(pageable);
    }

    /**
     * 입력한 제목만 페이징 처리를 해주는 메소드입니다.
     * @param pageable 페이징 처리를 위한 Pageable 객체
     * @param title 제목을 입력합니다.
     * @return 입력한 제목만 페이징 처리한 게시글을 반환합니다.
     */
    public Page<Board> findByTitle(Pageable pageable,String title){
        return boardRepository.findByTitleContainingOrderByCreatedDateDesc(pageable,title);
    }

    /**
     * 입력한 제목이나 내용만 페이징 처리를 해주는 메소드입니다.
     * @param pageable 페이징 처리를 위한 Pageable 객체
     * @param title 제목을 입력합니다.
     * @param contents 내용을 입력합니다.
     * @return 입력된 제목이나 내용만 페이징 처리한 게시글을 반환합니다.
     */
    public Page<Board> findByTitleOrContents(Pageable pageable,String title, String contents) {
        return boardRepository.findByTitleContainingOrContentsContainingOrderByCreatedDateDesc(pageable,title,contents);
    }

    /**
     * 입력한 uuid 값과 일치하는 게시글을 삭제하는 메소드입니다.
     * @param uuid 게시글의 uuid 값을 입력합니다.
     */
    @Transactional
    public void deleteByIdx(UUID uuid){
        boardRepository.deleteById(uuid);
    }

    /**
     * 입력된 uuid 값과 일치하는 게시글을 찾아주는 메소드입니다.
     *
     * @param uuid 게시글의 uuid 값을 입력합니다.
     * @return 입력된 uuid 값과 일치한 게시글을 반환합니다.
     * @throws NullPointerException 해당 id의 게시글이 없을 경우 예외를 발생시킵니다.
     */
    public Board findByIdx(UUID uuid){
        Optional<Board> boardOptional = boardRepository.findByIdx(uuid);
        return boardOptional.orElseThrow(() -> new NullPointerException());
    }

    /**
     * 요청한 게시글 정보로 기존 게시글을 수정해주는 메소드입니다.
     *
     * @param boardRequest 수정할 게시글 정보(제목, 내용, 종류)
     * @param board 기존 게시글
     */
    @Transactional
    public void updateBoard(BoardRequest boardRequest, Board board){
        board.setTitle(boardRequest.getTitle());
        board.setContents(boardRequest.getContents());
    }
}

