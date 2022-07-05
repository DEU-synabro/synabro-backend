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

/*
 * 이 클래스는 게시판 CRUD에 관한
 * 메소드들이 정의된 클래스이다.
 * @author tkfdkskarl56
 * @since 1.0
 */
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

    private static final String UPDATE_BOARD = "{\n" +
            "    \"code\" : 204\n" +
            "    \"message\" : \"게시글이 수정되었습니다.\"\n" +
            "}";

    private static final String UPDATE_NOT_BOARD = "{\n" +
            "    \"code\" : 404\n" +
            "    \"message\" : \"수정할 게시글이 없습니다.\"\n" +
            "}";

    /**
     * 제목, 제목+내용으로 글을 찾아주는 GET API 입니다.
     *
     * @param option (제목, 제목+내용) 중을 입력합니다.
     * @param keyword 검색할 단어를 입력합니다.
     * @return 제목 이나 제목+내용으로 검색한 게시판의 글을 반환합니다.
     */
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
        BoardPageResponse boardPageResponse;

        if(keyword==null){
            boards = boardService.findAll(pageable);
            addBoardListResponse(boards, boardListResponseList);
            boardPageResponse = new BoardPageResponse(pageable, boards, option, null, boardListResponseList);
        }else {
            if(searchOption=="제목+내용"){
                boards = boardService.findByTitleOrContents(pageable, keyword, keyword);
            }else {
                boards = boardService.findByTitle(pageable, keyword);
            }
            if (boards.getContent().isEmpty()) {
                BoardListResponse.addNullBoardListResponse(boardListResponseList);
            } else {
                addBoardListResponse(boards, boardListResponseList);
            }
            boardPageResponse = new BoardPageResponse(pageable, boards, option, keyword, boardListResponseList);
        }
        return new ResponseEntity<>(boardPageResponse, HttpStatus.OK);
    }

    /**
     * id 값으로 게시판의 글을 찾아주는 GET API 입니다.
     *
     * @param id 게시글의 UUID 값을 입력합니다.
     * @return 게시글의 정보를 반환합니다.
     */
    @Operation(tags = "Board", summary = "id 값으로 게시판 글을 찾습니다.",
            responses={
                    @ApiResponse(responseCode = "200", description = "id 값으로 게시판 글 정보 조회 성공",
                            content = @Content(schema = @Schema(implementation = Board.class)))
            })
    @GetMapping("/{board_id}")
    public ResponseEntity<Board> getBoard(@Parameter(description = "고유아이디") @PathVariable(name="board_id") UUID id){
        Board board = boardService.findByIdx(id);
        return new ResponseEntity<>(board, HttpStatus.OK);
    }

    /**
     * 게시글을 생성하는 POST API 입니다.
     *
     * @param boardRequest (제목, 내용, 종류) 클래스를 입력합니다.
     * @return 게시글 생성 성공 상태를 반환합니다.
     */
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
    public ResponseEntity<Board> boardCreate(@Parameter @RequestBody BoardRequest boardRequest){
        Board board = boardService.setBoard(boardRequest);
        return new ResponseEntity<>(board, HttpStatus.OK);
    }

    /**
     * 게시글을 삭제하는 DELETE API 입니다.
     *
     * @param id 게시글의 UUID 값을 입력합니다.
     * @return 게시글 삭제 성공 상태를 반환합니다.
     */
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
        try{
            boardService.deleteByIdx(id);
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.OK,"게시글이 삭제되었습니다."), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.NOT_FOUND,"삭제할 게시글이 없습니다."), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 게시글 수정하는 PATCH API 입니다.
     *
     * @param id 게시글의 UUID 값을 입력합니다.
     * @param boardRequest 수정할 (제목, 내용, 종류) 클래스를 입력합니다.
     * @return 게시글 수정 성공 상태를 반환합니다.
     */
    @Operation(tags = "Board", summary = "게시판 글을 수정합니다.",
            responses={
                    @ApiResponse(responseCode = "204", description = "게시판 글 수정 성공",
                            content = @Content(schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(value = UPDATE_BOARD))),
                    @ApiResponse(responseCode = "404", description = "수정할 글이 없음",
                            content = @Content(schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(value = UPDATE_NOT_BOARD)))
            })
    @PatchMapping("/update/{id}") // 게시판 수정
    public ResponseEntity<GeneralResponse> boardUpdate(@Parameter(description = "고유아이디") @PathVariable(name="id") UUID id,
                              @Parameter @RequestBody BoardRequest boardRequest){
        try{
            Board board = boardService.findByIdx(id);
            boardService.updateBoard(boardRequest, board);
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.OK, "게시글이 수정되었습니다"), HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.NOT_FOUND,"수정할 게시글이 없습니다."), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 페이징 처리할 게시글을 추가하는 메소드입니다.
     *
     * @param boards 페이징 처리할 게시글을 입력받습니다.
     * @param boardListResponseList 페이징 처리를 추가할 리스트를 입력받습니다.
     */
    private void addBoardListResponse(Page<Board> boards, List<BoardListResponse> boardListResponseList){
        if(boards.getSize()>=boards.getTotalElements()){
            for(int i=0; i<boards.getTotalElements(); i++){
                BoardListResponse boardListResponse = BoardListResponse.builder()
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
                BoardListResponse boardListResponse = BoardListResponse.builder()
                        .idx(boards.getContent().get(i).getIdx())
                        .title(boards.getContent().get(i).getTitle())
                        .createdDate(boards.getContent().get(i).getCreatedDate())
                        .build();
                boardListResponseList.add(boardListResponse);
                contentSize++;
            }
        }
    }
}
