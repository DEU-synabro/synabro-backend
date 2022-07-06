package com.deu.synabro.http.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 수정할 봉사 검수글 내용 정보를 담는 클래스
 *
 * @author tkfdkskarl56
 * @since 1.0
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class InspectionUpdateRequest {

    @Schema(description = "봉사 시간", example = "56")
    Short volunteerTime;

    @Schema(description = "내용",example = "내용")
    String contents;


}
