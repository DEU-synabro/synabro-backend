package com.deu.synabro.http.response;

import com.deu.synabro.controller.CertificationController;
import com.deu.synabro.entity.Certification;
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
@Schema(description = "봉사 인증 글")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CertificationPageResponse {
    private PagedModel<CertificationListResponse> contentsResponses;

    public CertificationPageResponse(Pageable pageable, Page<Certification> contentsPage, SearchOption searchOption, String keyword, List<CertificationListResponse> contentsResponseList) {
        PagedModel.PageMetadata pageMetadata =
                new PagedModel.PageMetadata(pageable.getPageSize(), contentsPage.getNumber(), contentsPage.getTotalElements());
        contentsResponses = PagedModel.of(contentsResponseList, pageMetadata);
        contentsResponses.add(linkTo(methodOn(CertificationController.class).getPagingCertification(pageable, searchOption, keyword)).withSelfRel());
    }
}
