package com.deu.synabro.http.request;

import com.deu.synabro.entity.Volunteer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class VolunteerRequest {

    @Schema(description = "유저 이름",example = "dnd01")
    String userId;

    @Schema(description = "제목",example = "제목")
    String title;

    @Schema(description = "설명",example = "설명")
    String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "설명", example = "2022-02-12T12:16:40.77")
    LocalDateTime endedDate;

    public Volunteer toEntity(){
        Volunteer volunteer = Volunteer.builder()
                                    .userId(userId)
                                    .title(title)
                                    .description(description)
                                    .endedDate(endedDate)
                                    .build();
        return volunteer;
    }
}
