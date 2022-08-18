package com.deu.synabro.http.request.offVolunteer;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OffVolunteerRequest {
    private String title;
    private String contents;
    private LocalDateTime startPeriod;
    private LocalDateTime endPeriod;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String tagName;
}
