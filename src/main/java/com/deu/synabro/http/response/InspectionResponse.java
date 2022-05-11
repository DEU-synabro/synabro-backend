package com.deu.synabro.http.response;

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
public class InspectionResponse {
    @Schema(description = "고유번호", example = "8857ba20-2cb7-407e-908c-b333cf1257c5")
    private UUID idx;

    @Schema(description = "아이디", example = "62ddd3ba-bbf0-469f-9c7d-f908df979c75")
    private UUID userId;

    @Schema(description = "아이디", example = "5e08d9a5-a117-4a81-91e8-a5a850752952")
    private UUID workId;

    @Schema(description = "아이디", example = "f3ae7fd1-63ff-41dd-8958-606d32d68a32")
    private UUID volunteerWorkId;

    @Schema(description = "봉사 요청 제목", example = "제목")
    private String workTitle;

    @Schema(description = "봉사 요청 설명", example = "설명")
    private String workContents;

    @Schema(description = "봉사 수행 설명", example = "설명")
    private String volunteerWorkContents;

    @Schema(description = "봉사 검수 설명", example = "설명")
    private String inspectionContents;

    @Schema(description = "봉사 시간", example = "56")
    Short volunteerTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "검수 생성 날짜")
    private LocalDateTime createdDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "검수 수정 날짜")
    private LocalDateTime updatedDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "검수 종료 날짜")
    private LocalDateTime endedDate;

}
