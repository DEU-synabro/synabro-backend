package com.deu.synabro.entity;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
@Table(name="docs")
public class Docs {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "docs_id", columnDefinition = "BINARY(16)")
    private UUID id;
    @Column(name="work_id")
    private String workId;
    @Column
    private String contents;
    @Column
    private String page;
    @Column(name="file_name")
    private String fileName;

    @Builder
    public Docs(String workId, String contents, String page, String fileName) {
        this.workId = workId;
        this.contents = contents;
        this.page = page;
        this.fileName = fileName;
    }
}
