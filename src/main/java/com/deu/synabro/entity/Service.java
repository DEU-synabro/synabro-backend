package com.deu.synabro.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name="service")
@RequiredArgsConstructor
public class Service extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "service_id", columnDefinition = "BINARY(16)")
    private UUID idx;

    @Column
    @Schema(description = "봉사 제목", example = "봉사 제목")
    private String title;

    @Column
    @Schema(description = "봉사 내용", example = "봉사 내용")
    private String contents;

    @Column
    @Schema(description = "봉사 모집 시작 기간", example = "2022-07-30")
    private String startPeriod;

    @Column
    @Schema(description = "봉사 모집 마감 기간", example = "2022-08-10")
    private String endPeriod;

    @Column
    @Schema(description = "봉사 수행 시작 날짜", example = "2022-08-14 12:00:00")
    private String startDate;

    @Column
    @Schema(description = "봉사 모집 종료 날짜", example = "2022-08-14 16:00:00")
    private String endDate;
}
