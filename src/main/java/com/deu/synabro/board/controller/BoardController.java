package com.deu.synabro.board.controller;

import com.deu.synabro.board.domain.BoardEntity;
import com.deu.synabro.board.service.BoardService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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

//    @GetMapping("/notice")
//    public List<BoardEntity> boardNoticeFind() {
//        return boardService.findByBoardType("notice");
//    }

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
