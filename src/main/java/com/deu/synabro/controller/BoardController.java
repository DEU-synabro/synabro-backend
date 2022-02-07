package com.deu.synabro.controller;

import com.deu.synabro.entity.Board;
import com.deu.synabro.http.response.BoardResponse;
import com.deu.synabro.http.request.BoardRequest;
import com.deu.synabro.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="Board", description = "게시판 API")
@RestController
@AllArgsConstructor
public class BoardController {

    @Autowired
    private BoardService boardService;

    @Operation(tags = "Board", summary = "모든 게시판의 글 정보를 반환합니다.",
        responses={
            @ApiResponse(responseCode = "200", description = "게시판 글 정보 조회 성공",
                    content = @Content(schema = @Schema(implementation = BoardResponse.class)))
    })
    @io.swagger.annotations.ApiResponses(
            @io.swagger.annotations.ApiResponse(
                    response = BoardResponse.class, message = "ok", code=200)
    )
    @GetMapping("/board")     //모든 게시판 찾기
    public List<Board> boardCheck(){
        return boardService.getAllBoard();
    }

    @Operation(tags = "Board", summary = "제목으로 글을 찾습니다.",
            responses={
                    @ApiResponse(responseCode = "200", description = "제목으로 글 정보 조회 성공",
                            content = @Content(schema = @Schema(implementation = BoardResponse.class)))
            })
    @io.swagger.annotations.ApiResponses(
            @io.swagger.annotations.ApiResponse(
                    response = BoardResponse.class, message = "ok", code=200)
    )
    @GetMapping("/title-board")   //제목으로 글 찾기
    public List<Board> boardTitleFind(@RequestParam(name="title") String title){
        return boardService.findByTitle(title);
    }

//    @GetMapping("/notice")
//    public List<BoardEntity> boardNoticeFind() {
//        return boardService.findByBoardType("notice");
//    }

    @Operation(tags = "Board", summary = "이름으로 게시판 글을 찾습니다.",
            responses={
                    @ApiResponse(responseCode = "200", description = "이름으로 글 정보 조회 성공",
                            content = @Content(schema = @Schema(implementation = BoardResponse.class)))
            })
    @io.swagger.annotations.ApiResponses(
            @io.swagger.annotations.ApiResponse(
                    response = BoardResponse.class, message = "ok", code=200)
    )
    @GetMapping("/userid-board")  //이름으로 게시판 찾기
    public List<Board> boardUser_idFind(@RequestParam(name="user_id") String user_id){
        return boardService.findByUser_id(user_id);
    }

    @Operation(tags = "Board", summary = "제목+내용으로 게시판 글을 찾습니다.",
            responses={
                    @ApiResponse(responseCode = "200", description = "제목+내용으로 글 정보 조회 성공",
                            content = @Content(schema = @Schema(implementation = BoardResponse.class)))
            })
    @io.swagger.annotations.ApiResponses(
            @io.swagger.annotations.ApiResponse(
                    response = BoardResponse.class, message = "ok", code=200)
    )
    @GetMapping("/content-board")  //제목+내용으로 게시판 찾기
    public List<Board> boardTitleOrContentFind(@RequestParam(name="title") String temp){
        return boardService.findByTitleOrContents(temp,temp);
    }

    @Operation(tags = "Board", summary = "게시판 글을 생성 합니다.",
        responses={
            @ApiResponse(responseCode = "200", description = "게시판 글 생성 성공",
                    content = @Content(schema = @Schema(implementation = BoardResponse.class)))
    })
    @io.swagger.annotations.ApiResponses(
            @io.swagger.annotations.ApiResponse(
                    response = BoardResponse.class, message = "ok", code=200)
    )
    @PostMapping("/board") // 게시판 생성
    public Board boardCreate(@Parameter @RequestBody BoardRequest reqBoard){
        Board boardEntity = boardService.setBoard(reqBoard);
        return boardEntity;
    }

    @Operation(tags = "Board", summary = "게시판 글을 삭제 합니다.",
            responses={
                    @ApiResponse(responseCode = "200", description = "게시판 글 삭제 성공",
                            content = @Content(schema = @Schema(implementation = BoardResponse.class)))
            })
    @io.swagger.annotations.ApiResponses(
            @io.swagger.annotations.ApiResponse(
                    response = BoardResponse.class, message = "ok", code=200)
    )
    @DeleteMapping("/board/{id}/{title}") // 게시판 삭제
    public List<Board> boardTitleDelete(@Parameter(description = "고유아이디") @PathVariable(name="id") Long id,
                                        @Parameter(description = "제목") @PathVariable(name="title") String title){
        return boardService.deleteByTitle(title);
    }

//    @GetMapping("/board/update/{id}/{title}")
//    public List<BoardEntity> boardUpdate(@PathVariable(name="id") Long id, @PathVariable(name="title") String title){
//        return boardService.findByIdAndTitle(id,title);
//    }

    @Operation(tags = "Board", summary = "게시판 글을 수정합니다.",
            responses={
                    @ApiResponse(responseCode = "200", description = "게시판 글 수정 성공",
                            content = @Content(schema = @Schema(implementation = BoardResponse.class)))
            })
    @io.swagger.annotations.ApiResponses(
            @io.swagger.annotations.ApiResponse(
                    response = BoardResponse.class, message = "ok", code=200)
    )
    @PostMapping("/board/update/{id}/{title}") // 게시판 수정
    public Board boardUpdate2(@Parameter(description = "고유아이디") @PathVariable(name="id") Long id,
                              @Parameter(description = "글제목") @PathVariable(name="title") String title,
                              @Parameter @RequestBody BoardRequest boardRequest){
        List<Board> boardEntities = boardService.findByIdAndTitle(id,title);
        return boardService.UpdateBoard(boardRequest, boardEntities.get(0));
    }
}
