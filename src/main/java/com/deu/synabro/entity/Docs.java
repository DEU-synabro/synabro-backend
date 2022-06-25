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

    @ManyToOne
    @JoinColumn(name="work_id")
    private Work work;

    @ManyToOne
    @JoinColumn(name="off_volunteer_id")
    private OffVolunteer offVolunteer;

    @Column(name="file_name")
    private String fileName;

    @Builder
    public Docs(Work work, String fileName, OffVolunteer offVolunteer) {
        this.work = work;
        this.fileName = fileName;
        this.offVolunteer = offVolunteer;
    }
}
