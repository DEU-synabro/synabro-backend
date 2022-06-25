package com.deu.synabro.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name="off_volunteer")
@RequiredArgsConstructor
public class OffVolunteer extends BaseTime {
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

    @Column(name = "start_period")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "봉사 모집 시작 기간", example = "2022-07-30")
    private String startPeriod;

    @Column(name = "end_period")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "봉사 모집 마감 기간", example = "2022-08-10")
    private String endPeriod;

    @Column(name = "start_date")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "봉사 수행 시작 날짜", example = "2022-08-14 12:00:00")
    private String startDate;

    @Column(name = "end_date")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "봉사 모집 종료 날짜", example = "2022-08-14 16:00:00")
    private String endDate;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "offVolunteer")
    private List<Docs> docsList = new ArrayList<>();

    public void addDocs(Docs docs){
        this.docsList.add(docs);
        docs.setOffVolunteer(this);
    }

    @Builder
    public OffVolunteer(String title, String contents, String startPeriod, String endPeriod, String startDate, String endDate) {
        this.title = title;
        this.contents = contents;
        this.startPeriod = startPeriod;
        this.endPeriod = endPeriod;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
