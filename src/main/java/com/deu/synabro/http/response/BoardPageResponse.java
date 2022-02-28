package com.deu.synabro.http.response;

import com.deu.synabro.controller.BoardController;
import com.deu.synabro.controller.MemberController;
import com.deu.synabro.entity.Board;
import com.deu.synabro.entity.Member;
import com.deu.synabro.entity.enums.SearchOption;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@Setter
@ToString
@Schema(description = "게시판")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BoardPageResponse {

    private PagedModel<Board> boards;

    public BoardPageResponse(Pageable pageable, Page<Board> boardPage, SearchOption searchOption, String keyword) {
        PagedModel.PageMetadata pageMetadata =
                new PagedModel.PageMetadata(pageable.getPageSize(), boardPage.getNumber(), boardPage.getTotalElements());
        boards = PagedModel.of(boardPage.getContent(), pageMetadata);
        boards.add(linkTo(methodOn(BoardController.class).getBoards(pageable, searchOption, keyword)).withSelfRel());
    }

}
