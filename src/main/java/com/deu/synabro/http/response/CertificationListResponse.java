package com.deu.synabro.http.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CertificationListResponse {
    @Schema(description = "고유번호", example = "8857ba20-2cb7-407e-908c-b333cf1257c5")
    private UUID idx;

    @Schema(description = "봉사 제목", example = "제목")
    private String title;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "Asia/Seoul")
    @Schema(description = "봉사 생성 날짜", example = "2022-04-07")
    private LocalDateTime createdDate;

    public static void addNullCertificationListResponse(List<CertificationListResponse> certificationListResponseList){
        CertificationListResponse certificationListResponse = CertificationListResponse.builder()
                .idx(null)
                .title(null)
                .createdDate(null)
                .build();
        certificationListResponseList.add(certificationListResponse);
    }
}
