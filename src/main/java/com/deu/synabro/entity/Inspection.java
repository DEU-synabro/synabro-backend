package com.deu.synabro.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Data
@RequiredArgsConstructor
@Entity
@Table(name="inspection")
@EntityListeners(AuditingEntityListener.class)
public class Inspection extends BaseTime implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "inspection_id", columnDefinition = "BINARY(16)")
    private UUID idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    @Schema(description = "봉사 검수자 아이디", example = "제목")
    private Member userId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "volunteer_work_id")
    private VolunteerWork volunteerWorkId;

    @Column
    private String contents;

    @Builder
    public Inspection(Member userId, VolunteerWork volunteerWorkId, String contents) {
        this.userId = userId;
        this.volunteerWorkId = volunteerWorkId;
        this.contents = contents;
    }
}
