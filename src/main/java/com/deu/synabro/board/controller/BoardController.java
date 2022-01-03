package com.deu.synabro.board.controller;

import com.deu.synabro.board.domain.entity.BoardEntity;
import com.deu.synabro.board.domain.repository.BoardRepository;
import com.deu.synabro.board.dto.BoardDTO;
import com.deu.synabro.board.service.BoardService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;


@RestController
@AllArgsConstructor
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/findAll")     //모든 게시판 찾기
    public List<BoardEntity> boardCheck(){
        return boardService.getAllBoard();
    }

    @GetMapping("/titlefind")   //제목으로 게시판 찾기
    public List<BoardEntity> boardTitleFind(@RequestParam(name="title") String title){
        return boardService.findByTitle(title);
    }

    @GetMapping("/user_idfind")  //이름으로 게시판 찾기
    public List<BoardEntity> boardUser_idFind(@RequestParam(name="user_id") String user_id){
        return boardService.findByUser_id(user_id);
    }

    @GetMapping("/titleorcontentfind")  //제목+내용으로 게시판 찾기
    public List<BoardEntity> boardTitleOrContentFind(@RequestParam(name="title") String temp){
        return boardService.findByTitleOrContents(temp,temp);
    }

    @PostMapping("/create") // 게시판 생성
    public BoardEntity boardCreate(@RequestBody BoardEntity reqBoard){
        BoardEntity boardEntity = boardService.setBoard(reqBoard);
        return boardEntity;
    }

    @DeleteMapping("/deleteboardtitle") // 게시판 삭제
    public List<BoardEntity> boardTitleDelete(@RequestParam(name="title") String title){
        return boardService.deleteByTitle(title);
    }

    @GetMapping("/update/{userid}/{title}")
    public List<BoardEntity> boardUpdate(@PathVariable(name="userid") String userid, @PathVariable(name="title") String title){
        return boardService.findByUser_idAndTitle(userid,title);
    }

    @PostMapping("/update/{userid}/{title}")
    public BoardEntity boardUpdate2(@PathVariable(name="userid") String userid, @PathVariable(name="title") String title,
                                          @RequestBody BoardEntity reqBoard){
        List<BoardEntity> boardEntities = boardService.findByUser_idAndTitle(userid,title);
        System.out.print(reqBoard);
        return boardService.UpdateBoard(reqBoard, boardEntities.get(0));
    }
}
