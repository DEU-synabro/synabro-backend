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

    @Column(name="file_name")
    private String fileName;

    @Builder
    public Docs(Work work, String fileName) {
        this.work = work;
        this.fileName = fileName;
    }
}
