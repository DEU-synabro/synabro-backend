package com.deu.synabro.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name="certification")
@RequiredArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Certification extends BaseTime implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "certification_id", columnDefinition = "BINARY(16)")
    private UUID idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "off_volunteer_id")
    @Schema(description = "봉사 uuid")
    private OffVolunteer offVolunteerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    @Schema(description = "봉사 요청자 아이디", example = "제목")
    private Member userId;

    @Column(columnDefinition = "VARCHAR(255)")
    private String title;

    @Column(columnDefinition = "VARCHAR(255)")
    private String contents;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "work")
    private List<Docs> docsList = new ArrayList<>();

    public void addDocs(Docs docs){
        this.docsList.add(docs);
        docs.setCertification(this);
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "work")
    private List<Video> videoList = new ArrayList<>();

    public void addVideo(Video video){
        this.videoList.add(video);
        video.setCertification(this);
    }

    @Builder
    public Certification(OffVolunteer offVolunteerId, Member userId, String title, String contents) {
        this.offVolunteerId = offVolunteerId;
        this.userId = userId;
        this.title = title;
        this.contents = contents;
    }
}
