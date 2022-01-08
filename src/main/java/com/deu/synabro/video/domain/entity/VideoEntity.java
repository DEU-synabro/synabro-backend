package com.deu.synabro.video.domain.entity;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
@Table(name="video")
public class VideoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="work_id")
    private String work_id;
    @Column
    private String contents;
    @Column
    private String page;
    @Column
    private String url;
    @Column(name="file_name")
    private String file_name;

    @Builder
    public VideoEntity(String work_id, String contents, String page, String url, String file_name) {
        this.work_id = work_id;
        this.contents = contents;
        this.page = page;
        this.url = url;
        this.file_name = file_name;
    }
}
