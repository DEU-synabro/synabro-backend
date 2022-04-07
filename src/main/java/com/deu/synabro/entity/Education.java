package com.deu.synabro.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "education_id", columnDefinition = "BINARY(16)")
    @Schema(description = "교육 콘텐츠 ID", nullable = false, example = "468cc9b5-bca2-494d-9f5c-f00a1af81696")
    private UUID idx;

    @Column(columnDefinition = "VARCHAR(50)")
    @Schema(description = "교육 콘텐츠 제목", nullable = false, example = "제목글, 바닥글 적기")
    private String title;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "교육 콘텐츠 내용", nullable = false, example = "제목글과 바닥글을 적는 방법을 안내합니다.")
    private String contents;

    @OneToMany
    @JoinColumn(name = "education_docs_id")
    List<EducationDocs> educationDocs;

    @OneToMany
    @JoinColumn(name = "education_video_id")
    List<EducationVideo> educationVideos;
}
