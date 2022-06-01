package com.deu.synabro.http.response.member;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WorkHistoryDetailResponse extends WorkHistoryResponse {
    private final String content;
    private final String file;
    private final WorkHistoryResponse beforeWork;
    private final WorkHistoryResponse afterWork;

    public WorkHistoryDetailResponse() {
        super("VOLUNTEER", UUID.randomUUID(), "동영상 자막 작업 부탁드립니다.", LocalDate.now());
        this.content = "유튜브 '시간과 공간의 개념' 동영상에 자막 작업을 부탁드립니다.\n약 10분가략의 영상이며 한달 이내로 작업이 진행되면 \n좋을 것 같습니다.\n\n영상은 아래의 링크를 확인해주세요.\nhttps://www.youtube.com/watch?v=CfPx-ZQ1\n";
        this.file = "http://localhost:8080/file/downloads/"+UUID.randomUUID();
        this.beforeWork = WorkHistoryResponse.builder()
                .id(UUID.randomUUID())
                .type("VOLUNTEER")
                .title("[도서] 달라구트 꿈 백화점 전자화 요청")
                .date(LocalDate.now())
                .build();
        this.afterWork = WorkHistoryResponse.builder()
                .id(UUID.randomUUID())
                .type("VOLUNTEER")
                .title("사진에 찍힌 문서를 PDF 파일로 제작해주세요.")
                .date(LocalDate.now()).build();
    }
}

