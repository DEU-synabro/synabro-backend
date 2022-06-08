package com.deu.synabro.http.response.member;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WorkHistoryDetailResponse extends WorkHistoryResponse {
    private final String work_content;
    private final String result;
    private WorkHistoryResponse beforeWork;
    private WorkHistoryResponse afterWork;

    public WorkHistoryDetailResponse() {
        super(null, null, null, null);
        this.work_content = null;
        this.result = null;
        this.beforeWork = null;
        this.afterWork = null;
    }

    public WorkHistoryDetailResponse(String type, UUID id, String work_title, LocalDateTime workDate, String work_content, String result) {
        super(type, id, work_title, workDate);
        this.work_content = work_content;
        this.result = result;
        this.beforeWork = null;
        this.afterWork = null;
    }

    public void setBeforeWork(WorkHistoryResponse work) {
        this.beforeWork = work;
    }

    public void setAfterWork(WorkHistoryResponse work) {
        this.afterWork = work;
    }
}

