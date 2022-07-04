package com.deu.synabro.controller;

import com.deu.synabro.entity.Board;
import com.deu.synabro.entity.enums.SearchOption;
import com.deu.synabro.http.response.*;
import com.deu.synabro.http.request.BoardRequest;
import com.deu.synabro.service.BoardService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Tag(name="Board", description = "게시판 API")
@RestController
@AllArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {

    @Autowired
    private BoardService boardService;

    private static final String DELETE_BOARD = "{\n" +
            "    \"code\" : 204\n" +
            "    \"message\" : \"게시글이 삭제되었습니다.\"\n" +
            "}";

    private static final String DELETE_NOT_BOARD = "{\n" +
            "    \"code\" : 404\n" +
            "    \"message\" : \"삭제할 게시글이 없습니다.\"\n" +
            "}";

    @Operation(tags = "Board", summary = "제목, 제목+내용으로 글을 찾습니다.",
            responses={
                    @ApiResponse(responseCode = "200", description = "제목, 제목+내용으로 글 정보 조회 성공",
                            content = @Content(schema = @Schema(implementation = BoardPageResponse.class)))
            })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "페이지 번호", dataType = "integer", paramType = "query", defaultValue = "0"),
            @ApiImplicitParam(name = "size", value = "한 페이지당 불러올 콘텐츠 개수", dataType = "integer", paramType = "query", defaultValue = "10"),
            @ApiImplicitParam(name = "sort", value = "정렬방법(id,desc,asc)", dataType = "string", paramType = "query", defaultValue = "asc")
    })
    @GetMapping("")   //제목으로 글 찾기
    public ResponseEntity<BoardPageResponse> getBoards(@PageableDefault Pageable pageable,
                                                       @RequestParam(name="searchOption", required = false, defaultValue = "title") SearchOption option,
                                                            @RequestParam(name="keyword", required = false) String keyword){
        Page<Board> boards;
        String searchOption=option.getValue();
        List<BoardListResponse> boardListResponseList = new ArrayList<>();
        BoardListResponse boardListResponse;
        BoardPageResponse boardPageResponse;

        if(keyword==null){
            boards = boardService.findAll(pageable);
            if(boards.getSize()>=boards.getTotalElements()){
                for(int i=0; i<boards.getTotalElements(); i++){
                    boardListResponse = BoardListResponse.builder()
                            .idx(boards.getContent().get(i).getIdx())
                            .title(boards.getContent().get(i).getTitle())
                            .createdDate(boards.getContent().get(i).getCreatedDate())
                            .build();
                    boardListResponseList.add(boardListResponse);
                }
            }else {
                int contentSize = boards.getSize()*boards.getNumber();
                for(int i=0; i<boards.getSize();i++){
                    if(contentSize>=boards.getTotalElements())
                        break;
                    boardListResponse = BoardListResponse.builder()
                            .idx(boards.getContent().get(i).getIdx())
                            .title(boards.getContent().get(i).getTitle())
                            .createdDate(boards.getContent().get(i).getCreatedDate())
                            .build();
                    boardListResponseList.add(boardListResponse);
                    contentSize++;
                }
            }
            boardPageResponse = new BoardPageResponse(pageable, boards, option, null, boardListResponseList);
            return new ResponseEntity<>(boardPageResponse, HttpStatus.OK);
        }else {
            if(searchOption=="제목+내용"){
                boards = boardService.findByTitleOrContents(pageable, keyword, keyword);
            }else {
                boards = boardService.findByTitle(pageable, keyword);
            }
            if (boards.getContent().isEmpty()) {
                boardListResponse = BoardListResponse.builder()
                        .idx(null)
                        .title(null)
                        .createdDate(null)
                        .build();
                boardListResponseList.add(boardListResponse);
            } else {
                if(boards.getSize()>=boards.getTotalElements()){
                    for(int i=0; i<boards.getTotalElements(); i++){
                        boardListResponse = BoardListResponse.builder()
                                .idx(boards.getContent().get(i).getIdx())
                                .title(boards.getContent().get(i).getTitle())
                                .createdDate(boards.getContent().get(i).getCreatedDate())
                                .build();
                        boardListResponseList.add(boardListResponse);
                    }
                }else {
                    int contentSize = boards.getSize()*boards.getNumber();
                    for(int i=0; i<boards.getSize();i++){
                        if(contentSize>=boards.getTotalElements())
                            break;
                        boardListResponse = BoardListResponse.builder()
                                .idx(boards.getContent().get(i).getIdx())
                                .title(boards.getContent().get(i).getTitle())
                                .createdDate(boards.getContent().get(i).getCreatedDate())
                                .build();
                        boardListResponseList.add(boardListResponse);
                        contentSize++;
                    }
                }
            }
            boardPageResponse = new BoardPageResponse(pageable, boards, option, keyword, boardListResponseList);
            return new ResponseEntity<>(boardPageResponse, HttpStatus.OK);
        }
    }

    @Operation(tags = "Board", summary = "id 값으로 게시판 글을 찾습니다.",
            responses={
                    @ApiResponse(responseCode = "200", description = "id 값으로 게시판 글 정보 조회 성공",
                            content = @Content(schema = @Schema(implementation = Board.class)))
            })
    @GetMapping("/{id}")   //제목으로 글 찾기
    public ResponseEntity<Board> getBoard(@Parameter(description = "고유아이디") @PathVariable(name="id") UUID id){
        Board boardEntities = boardService.findByIdx(id);
        return new ResponseEntity<>(boardEntities,HttpStatus.OK);
    }

    @Operation(tags = "Board", summary = "게시판 글을 생성 합니다.",
            responses={
                    @ApiResponse(responseCode = "200", description = "게시판 글 생성 성공",
                            content = @Content(schema = @Schema(implementation = Board.class)))
            })
    @io.swagger.annotations.ApiResponses(
            @io.swagger.annotations.ApiResponse(
                    response = Board.class, message = "ok", code=200)
    )
    @PostMapping("") // 게시판 생성
    public ResponseEntity<Board> boardCreate(@Parameter @RequestBody BoardRequest reqBoard){
        Board board = boardService.setBoard(reqBoard);
        return new ResponseEntity<>(board, HttpStatus.OK);
    }

    @Operation(tags = "Board", summary = "게시판 글을 삭제 합니다.",
            responses={
                    @ApiResponse(responseCode = "204", description = "게시판 글 삭제 성공",
                            content = @Content(schema = @Schema(implementation = GeneralResponse.class),
                                                examples = @ExampleObject(value = DELETE_BOARD))),
                    @ApiResponse(responseCode = "404", description = "삭제할 글이 없음",
                            content = @Content(schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(value = DELETE_NOT_BOARD)))
            })
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}") // 게시판 삭제
    public ResponseEntity<GeneralResponse> boardTitleDelete(@Parameter(description = "고유아이디") @PathVariable(name="id") UUID id){
        if(boardService.deleteById(id)){
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.OK,"게시글이 삭제되었습니다."), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.NOT_FOUND,"삭제할 게시글이 없습니다."), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(tags = "Board", summary = "게시판 글을 수정합니다.",
            responses={
                    @ApiResponse(responseCode = "200", description = "게시판 글 수정 성공",
                            content = @Content(schema = @Schema(implementation = Board.class)))
            })
    @io.swagger.annotations.ApiResponses(
            @io.swagger.annotations.ApiResponse(
                    response = Board.class, message = "ok", code=200)
    )
    @PatchMapping("/update/{id}") // 게시판 수정
    public ResponseEntity<GeneralResponse> boardUpdate(@Parameter(description = "고유아이디") @PathVariable(name="id") UUID id,
                              @Parameter @RequestBody BoardRequest boardRequest){
        Board board = boardService.findByIdx(id);
        System.out.println(board.getIdx());
        System.out.println(board.getContents());
        System.out.println(board);
        if(board==null){
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.NOT_FOUND,"수정할 글이 없습니다."), HttpStatus.NOT_FOUND);
        }else{
            boardService.updateBoard(boardRequest, board);
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.OK, "글이 수정되었습니다"), HttpStatus.OK);
        }

    }
}
