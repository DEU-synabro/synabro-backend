package com.deu.synabro.http.request;

import com.deu.synabro.entity.Docs;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class DocsRequest {
    private String workId;
    private String contents;
    private String page;
    private String fileName;

    public Docs toEntity(){
        Docs docsEntity = Docs.builder()
                .workId(workId)
                .contents(contents)
                .page(page)
                .fileName(fileName)
                .build();
        return docsEntity;
    }

    @Builder
    public DocsRequest(String workId, String contents, String page, String fileName) {
        this.workId = workId;
        this.contents = contents;
        this.page = page;
        this.fileName = fileName;
    }
}
