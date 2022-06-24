package com.deu.synabro.entity;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@Entity
@Table(name="video")
@EntityListeners(AuditingEntityListener.class)
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "video_id", columnDefinition = "BINARY(16)")
    private UUID idx;

    @ManyToOne
    @JoinColumn(name="work_id")
    private Work work;

    @Column(name="file_name")
    private String fileName;

    @Builder
    public Video(Work work, String fileName) {
        this.work = work;
        this.fileName = fileName;
    }

}
