package com.deu.synabro.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
public class EducationDocs {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "education_docs_id", columnDefinition = "BINARY(16)")
    @Schema(description = "교육 콘텐츠 문서 ID", nullable = false, example = "468cc9b5-bca2-494d-9f5c-f00a1af81696")
    private UUID idx;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "교육 콘텐츠 문서 내용", nullable = false, example = "교육 콘텐츠 내용이 텍스트일 경우입니다.")
    private String contents;

    @Column(name="file_name")
    @Schema(description = "교육 콘텐츠 문서 이름", nullable = false, example = "교육 콘텐츠 문서의 이름입니다.")
    private String fileName;
}
