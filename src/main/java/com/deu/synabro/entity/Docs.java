package com.deu.synabro.entity;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
@Entity
@Table(name="docs")
public class Docs {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "docs_id", columnDefinition = "BINARY(16)")
    private UUID idx;

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
    public Docs(Work work, String fileName, OffVolunteer offVolunteer, Certification certification) {
        this.work = work;
        this.fileName = fileName;
        this.offVolunteer = offVolunteer;
        this.certification = certification;
    }
}
