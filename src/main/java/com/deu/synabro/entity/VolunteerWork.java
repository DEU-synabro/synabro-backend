package com.deu.synabro.entity;

import com.deu.synabro.entity.enums.PerformType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

/**
 * 봉사 수행글 내용을 담든 Entity
 *
 * @author tkfdkskarl56
 * @since 1.0
 */
@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name="volunteer")
@EntityListeners(AuditingEntityListener.class)
public class VolunteerWork extends BaseTime implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "volunteer_work_id", columnDefinition = "BINARY(16)")
    private UUID idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    @Schema(description = "봉사 수행자 아이디", example = "제목")
    private Member userId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="work_id")
    private Work workId;

    @Column
    @Schema(description = "작업 진행 공간", example = "작업 내용")
    private String contents;

    @Enumerated(value=EnumType.STRING)
    @Column(name="perform_type")
    @Schema(description = "상태")
    private PerformType performType;

    @Builder
    public VolunteerWork(UUID idx, Member userId, Work workId, String contents, PerformType performType) {
        this.idx = idx;
        this.userId = userId;
        this.workId = workId;
        this.contents = contents;
        this.performType = performType;
    }
}
