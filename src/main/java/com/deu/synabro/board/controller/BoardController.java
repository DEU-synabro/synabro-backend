package com.deu.synabro.board.controller;

import com.deu.synabro.board.domain.entity.BoardEntity;
import com.deu.synabro.board.domain.repository.BoardRepository;
import com.deu.synabro.board.dto.BoardDTO;
import com.deu.synabro.board.service.BoardService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Column;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/findAll")
    public List<BoardEntity> boardCheck(){
        return boardService.getAllBoard();
    }

    @GetMapping("/titlefind")
    public List<BoardEntity> boardTitleFind(@RequestParam(name="title") String title){
        return boardService.findByTitle(title);
    }

    @GetMapping("/writerfind")
    public List<BoardEntity> boardWriterFind(@RequestParam(name="writer") String writer){
        return boardService.findByWriter(writer);
    }

    @PostMapping("/create")
    public BoardEntity boardCreate(@RequestBody BoardEntity reqBoard){
        BoardEntity boardEntity = boardService.setBoard(reqBoard);
        return boardEntity;
    }

    @DeleteMapping("/deleteboardtitle")
    public List<BoardEntity> boardTitleDelete(@RequestParam(name="title") String title){
        return boardService.deleteByTitle(title);
    }
}
