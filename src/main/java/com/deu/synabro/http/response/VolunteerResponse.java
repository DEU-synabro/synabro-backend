package com.deu.synabro.http.response;

import com.deu.synabro.entity.Volunteer;
import com.deu.synabro.entity.enums.BoardType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class VolunteerResponse {
    @Schema(description = "고유번호", example = "8857ba20-2cb7-407e-908c-b333cf1257c5")
    private UUID id;

    @Schema(description = "아이디", example = "dnd01")
    private String userId;

    @Schema(description = "봉사 제목", example = "제목")
    private String title;

    @Schema(description = "봉사 설명", example = "설명")
    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "봉사 생성 날짜")
    private LocalDateTime created_date;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "봉사 수정 날짜")
    private LocalDateTime updated_date;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "봉사 종료 날짜")
    private LocalDateTime ended_date;


//    public VolunteerResponse(Volunteer volunteer){
//        this.id = volunteer.getId();
//        this.userId = volunteer.getUserId();
//        this.title = volunteer.getTitle();
//        this.description = volunteer.getDescription();
//        this.created_date = volunteer.getCreatedDate();
//        this.updated_date = volunteer.getModifiedDate();
//        this.ended_date = volunteer.getEndedDate();
//    }
}
