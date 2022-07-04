package com.deu.synabro.http.request;

import com.deu.synabro.entity.Work;
import com.deu.synabro.entity.Member;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WorkRequest {

    @Schema(description = "제목",example = "제목")
    String title;

    @Schema(description = "내용",example = "내용")
    String contents;

    @Schema(description = "봉사 시간", example = "56")
    Short volunteerTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "설명", example = "2022-02-12T12:16:40.77")
    LocalDateTime endedDate;

    public Work toEntity(UUID uuid){
        Work work = Work.builder()
                .userId(new Member(uuid))
                .title(title)
                .contents(contents)
                .endedDate(endedDate)
                .volunteerTime(volunteerTime)
                .build();
        return work;
    }
}
