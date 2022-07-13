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

    @ManyToOne
    @JoinColumn(name="work_id")
    private Work work;

    @ManyToOne
    @JoinColumn(name="off_volunteer_id")
    private OffVolunteer offVolunteer;

    @Column(name="file_name")
    private String fileName;

    @Builder
    public Video(Work work, String fileName, OffVolunteer offVolunteer) {
        this.work = work;
        this.fileName = fileName;
        this.offVolunteer = offVolunteer;
    }

}
