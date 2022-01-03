package com.deu.synabro.docs.domain.entity;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
@Table(name="docs")
public class DocsEntity {
    @Id
    @Column(name="workd_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String work_id;
    @Column
    private String contents;
    @Column
    private String page;
    @Column(name="file_name")
    private String file_name;

    @Builder
    public DocsEntity(String work_id, String contents, String page, String file_name) {
        this.work_id = work_id;
        this.contents = contents;
        this.page = page;
        this.file_name = file_name;
    }
}
