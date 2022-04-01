package com.deu.synabro.http.response;

import com.deu.synabro.controller.VolunteerWorkController;
import com.deu.synabro.entity.VolunteerWork;
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

@Getter
@Setter
@ToString
@Schema(description = "봉사글")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class VolunteerWorkPageResponse {
    private PagedModel<VolunteerListResponse> volunteers;

    public VolunteerWorkPageResponse(Pageable pageable, Page<VolunteerWork> volunteerPage, SearchOption searchOption, String keyword, List<VolunteerListResponse> volunteerResponseList) {
        PagedModel.PageMetadata pageMetadata =
                new PagedModel.PageMetadata(pageable.getPageSize(), volunteerPage.getNumber(), volunteerPage.getTotalElements());
        volunteers = PagedModel.of(volunteerResponseList, pageMetadata);
        volunteers.add(linkTo(methodOn(VolunteerWorkController.class).getVolunteerWorks(pageable, searchOption, keyword)).withSelfRel());
    }
}
