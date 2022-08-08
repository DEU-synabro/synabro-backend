package com.deu.synabro.http.response;

import com.deu.synabro.controller.BoardController;
import com.deu.synabro.entity.Board;
import com.deu.synabro.entity.enums.BoardType;
import com.deu.synabro.entity.enums.SearchOption;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * 페이징 처리한 게시글을 담는 클래스
 */
@Getter
@Setter
@ToString
@Schema(description = "게시판")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BoardPageResponse {
    private PagedModel<BoardListResponse> boards;

    public BoardPageResponse(Pageable pageable, Page<Board> boardPage, SearchOption searchOption, String keyword, List<BoardListResponse> boardListResponseList, BoardType boardType) {
        PagedModel.PageMetadata pageMetadata =
                new PagedModel.PageMetadata(pageable.getPageSize(), boardPage.getNumber(), boardPage.getTotalElements());
        boards = PagedModel.of(boardListResponseList, pageMetadata);
        boards.add(linkTo(methodOn(BoardController.class).getBoards(pageable, searchOption, keyword, boardType)).withSelfRel());
    }
}
