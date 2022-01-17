package com.deu.synabro.docs.dto;

import com.deu.synabro.docs.domain.entity.DocsEntity;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class DocsDTO {
    private String work_id;
    private String contents;
    private String page;
    private String file_name;

    public DocsEntity toEntity(){
        DocsEntity docsEntity = DocsEntity.builder()
                .work_id(work_id)
                .contents(contents)
                .page(page)
                .file_name(file_name)
                .build();
        return docsEntity;
    }

    @Builder
    public DocsDTO(String work_id, String contents, String page, String file_name) {
        this.work_id = work_id;
        this.contents = contents;
        this.page = page;
        this.file_name = file_name;
    }
}
