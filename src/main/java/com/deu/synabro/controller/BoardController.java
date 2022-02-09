package com.deu.synabro.controller;

import com.deu.synabro.entity.Board;
import com.deu.synabro.http.response.BoardPageResponse;
import com.deu.synabro.http.request.BoardRequest;
import com.deu.synabro.http.response.BoardResponse;
import com.deu.synabro.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name="Board", description = "게시판 API")
@RestController
@AllArgsConstructor
public class BoardController {

    @Autowired
    private BoardService boardService;

//    @Operation(tags = "Board", summary = "모든 게시판의 글 정보를 반환합니다.",
//        responses={
//            @ApiResponse(responseCode = "200", description = "게시판 글 정보 조회 성공",
//                    content = @Content(schema = @Schema(implementation = BoardResponse.class)))
//    })
//    @io.swagger.annotations.ApiResponses(
//            @io.swagger.annotations.ApiResponse(
//                    response = BoardResponse.class, message = "ok", code=200)
//    )
//    @GetMapping("/allboard")     //모든 게시판 찾기
//    public Page<Board> getAllBoard(@PageableDefault Pageable pageable){
//        return boardService.findAll(pageable);
//    }

    @Operation(tags = "Board", summary = "제목, 사용자, 제목+내용으로 글을 찾습니다.",
            responses={
                    @ApiResponse(responseCode = "200", description = "제목, 사용자, 제목+내용으로 글 정보 조회 성공",
                            content = @Content(schema = @Schema(implementation = BoardPageResponse.class)))
            })
    @io.swagger.annotations.ApiResponses(
            @io.swagger.annotations.ApiResponse(
                    response = BoardPageResponse.class, message = "ok", code=200)
    )
    @GetMapping("/board")   //제목으로 글 찾기
    public ResponseEntity<BoardPageResponse> boardTitleFind(@PageableDefault Pageable pageable,
                                                            @RequestParam(name="searchOption", defaultValue = "title") String searchOption,
                                                            @RequestParam(name="keyword") String keyword){
        if(searchOption.equals("title")){
            BoardPageResponse boardResponse = new BoardPageResponse(boardService.findByTitle(pageable,keyword));
            return new ResponseEntity<>(boardResponse, HttpStatus.OK);
        }
        else if(searchOption.equals("userid")){
            BoardPageResponse boardResponse = new BoardPageResponse(boardService.findByUser_id(pageable,keyword));
            return new ResponseEntity<>(boardResponse, HttpStatus.OK);
        }else if(searchOption=="title-content"){
            BoardPageResponse boardResponse = new BoardPageResponse(boardService.findByTitleOrContents(pageable,keyword,keyword));
            return new ResponseEntity<>(boardResponse, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
    public List<Board> boardTitleDelete(@Parameter(description = "고유아이디") @PathVariable(name="id") UUID id,
                                        @Parameter(description = "제목") @PathVariable(name="title") String title){
        return boardService.deleteByTitle(id, title);
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
    public Board boardUpdate2(@Parameter(description = "고유아이디") @PathVariable(name="id") UUID id,
                              @Parameter(description = "글제목") @PathVariable(name="title") String title,
                              @Parameter @RequestBody BoardRequest boardRequest){
        List<Board> boardEntities = boardService.findByIdAndTitle(id,title);
        return boardService.UpdateBoard(boardRequest, boardEntities.get(0));
    }
}