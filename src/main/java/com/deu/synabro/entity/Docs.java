package com.deu.synabro.entity;

import com.deu.synabro.http.request.DocsRequest;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
@AllArgsConstructor
@Table(name="docs")
public class Docs {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "docs_id", columnDefinition = "BINARY(16)")
    private UUID id;
    @ManyToOne
    @JoinColumn(name="Volunteer_id")
    private Volunteer workId;
    @Column(name="file_name")
    private String fileName;

    public Docs(Volunteer workId, String fileName){
        this.workId = workId;
        this.fileName = fileName;
    }
}
