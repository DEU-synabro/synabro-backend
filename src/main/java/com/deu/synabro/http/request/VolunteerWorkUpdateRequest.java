package com.deu.synabro.http.request;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 수정할 봉사 수행글 내용 정보를 담는 클래스
 *
 * @author tkfdkskarl56
 * @since 1.0
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class VolunteerWorkUpdateRequest {

    @Schema(description = "내용",example = "내용")
    String contents;

}
