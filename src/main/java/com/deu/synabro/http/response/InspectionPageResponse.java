package com.deu.synabro.http.response;

import com.deu.synabro.controller.InspectionController;
import com.deu.synabro.entity.Inspection;
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
@Schema(description = "점검글")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class InspectionPageResponse {
    private PagedModel<InspectionListResponse> inspectionResponses;

    public InspectionPageResponse(Pageable pageable, Page<Inspection> inspectionPage, SearchOption searchOption, String keyword, List<InspectionListResponse> inspectionResponseList) {
        PagedModel.PageMetadata pageMetadata =
                new PagedModel.PageMetadata(pageable.getPageSize(), inspectionPage.getNumber(), inspectionPage.getTotalElements());
        inspectionResponses = PagedModel.of(inspectionResponseList, pageMetadata);
        inspectionResponses.add(linkTo(methodOn(InspectionController.class).getInspections(pageable, searchOption, keyword)).withSelfRel());
    }
}
