package com.deu.synabro.entity;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@Entity
@Table(name="video")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "video_id", columnDefinition = "BINARY(16)")
    private UUID idx;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="work_id")
    private Work work;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="off_volunteer_id")
    private OffVolunteer offVolunteer;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="certification_id")
    private Certification certification;

    @Column(name="file_name")
    private String fileName;

    @Builder
    public Video(Board board, Work work, OffVolunteer offVolunteer, Certification certification, String fileName) {
        this.board = board;
        this.work = work;
        this.offVolunteer = offVolunteer;
        this.certification = certification;
        this.fileName = fileName;
    }
}
