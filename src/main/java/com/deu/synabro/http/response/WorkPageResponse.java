package com.deu.synabro.http.response;

import com.deu.synabro.controller.WorkController;
import com.deu.synabro.entity.Work;
import com.deu.synabro.entity.enums.SearchOption;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * 페이징 처리한 봉사 요청글을 담는 클래스
 *
 * @author tkfdkskarl56
 * @since 1.0
 */
@Getter
@Setter
@ToString
@Schema(description = "봉사 요청 글")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WorkPageResponse {
    private PagedModel<WorkListResponse> contentsResponses;

    public WorkPageResponse(Pageable pageable, Page<Work> contentsPage, SearchOption searchOption, String keyword, List<WorkListResponse> contentsResponseList) {
        PagedModel.PageMetadata pageMetadata =
                new PagedModel.PageMetadata(pageable.getPageSize(), contentsPage.getNumber(), contentsPage.getTotalElements());
        contentsResponses = PagedModel.of(contentsResponseList, pageMetadata);
        contentsResponses.add(linkTo(methodOn(WorkController.class).getPagingContents(pageable, searchOption, keyword)).withSelfRel());
    }
}
