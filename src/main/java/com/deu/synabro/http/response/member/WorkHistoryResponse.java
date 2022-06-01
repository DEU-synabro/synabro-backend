package com.deu.synabro.http.response.member;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WorkHistoryResponse {
    private final String type;
    private final UUID id;
    private final String title;
    private final LocalDate date;

    @Builder
    public WorkHistoryResponse(String type, UUID id, String title, LocalDate date) {
        this.type = type;
        this.id = id;
        this.title = title;
        this.date = date;
    }
}
