package com.deu.synabro.entity;

import com.deu.synabro.entity.enums.ApprovalType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name="off_volunteer")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@RequiredArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class OffVolunteer extends BaseTime implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "off_volunteer_id", columnDefinition = "BINARY(16)")
    private UUID idx;

    @Column
    @Schema(description = "봉사 제목", example = "봉사 제목")
    private String title;

    @Column
    @Schema(description = "봉사 내용", example = "봉사 내용")
    private String contents;

    @Column
    @Schema(description = "봉사 태그", example = "봉사 태그")
    private String tagName;

    @Enumerated(value=EnumType.STRING)
    @Column
    @Schema(description = "인증 글 허용", example = "true 일 경우 해당 인증글 작성 가능")
    private ApprovalType approvalType;

    @Column(name = "start_period")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startPeriod;

    @Column(name = "end_period")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endPeriod;

    @Column(name = "start_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "offVolunteer")
    private List<Docs> docsList = new ArrayList<>();

    public void addDocs(Docs docs){
        this.docsList.add(docs);
        docs.setOffVolunteer(this);
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "offVolunteer")
    private List<Video> videoList = new ArrayList<>();

    public void addVideo(Video video){
        this.videoList.add(video);
        video.setOffVolunteer(this);
    }

    @Builder
    public OffVolunteer(String title, String contents, LocalDateTime startPeriod, LocalDateTime endPeriod, LocalDateTime startDate, LocalDateTime endDate, String tagName, ApprovalType approvalType) {
        this.title = title;
        this.contents = contents;
        this.startPeriod = startPeriod;
        this.endPeriod = endPeriod;
        this.startDate = startDate;
        this.endDate = endDate;
        this.tagName = tagName;
        this.approvalType = approvalType;
    }

    public OffVolunteer(UUID idx) {
        this.idx = idx;
    }
}
