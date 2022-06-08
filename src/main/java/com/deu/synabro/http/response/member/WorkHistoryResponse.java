package com.deu.synabro.http.response.member;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WorkHistoryResponse {
    private final String type;
    private final UUID id;
    private final String workTitle;
    @DateTimeFormat(pattern = "yyyy.MM.dd")
    private final LocalDateTime date;

    @Builder
    public WorkHistoryResponse(String type, UUID id, String workTitle, LocalDateTime date) {
        this.type = type;
        this.id = id;
        this.workTitle = workTitle;
        this.date = date;
    }
}
