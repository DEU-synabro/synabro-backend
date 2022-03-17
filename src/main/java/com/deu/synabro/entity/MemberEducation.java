package com.deu.synabro.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
public class MemberEducation extends BaseTime {
    @Id
    @Column(name = "member_education_id", columnDefinition = "BINARY(16)")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(description = "사용자가 진행한 교육 상태에 대한 ID", nullable = false, example = "468cc9b5-bca2-494d-9f5c-f00a1af81696")
    private UUID idx;

    @Column(name = "work_process")
    private Integer workProcess;

    @Column(name = "ended_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endedDate;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "education_id")
    private Education education;
}
