package com.deu.synabro.http.request.offVolunteer;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OffVolunteerRequest {
    private String title;
    private String contents;
    private LocalDateTime startPeriod;
    private LocalDateTime endPeriod;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
