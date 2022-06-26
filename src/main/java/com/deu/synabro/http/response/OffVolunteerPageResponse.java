package com.deu.synabro.http.response;

import com.deu.synabro.controller.OffVolunteerController;
import com.deu.synabro.controller.WorkController;
import com.deu.synabro.entity.OffVolunteer;
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
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@Setter
//@ToString
@Schema(description = "봉사 모집 글")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OffVolunteerPageResponse {
    private PagedModel<OffVolunteerListResponse> offVolunteerListResponses;

    public OffVolunteerPageResponse(Pageable pageable, Page<OffVolunteer> contentsPage, SearchOption searchOption, String keyword, List<OffVolunteerListResponse> contentsResponseList) {
        PagedModel.PageMetadata pageMetadata =
                new PagedModel.PageMetadata(pageable.getPageSize(), contentsPage.getNumber(), contentsPage.getTotalElements());
        offVolunteerListResponses = PagedModel.of(contentsResponseList, pageMetadata);
        offVolunteerListResponses.add(linkTo(methodOn(OffVolunteerController.class).getPagingOffVolunteer(pageable, searchOption, keyword)).withSelfRel());
    }
}
