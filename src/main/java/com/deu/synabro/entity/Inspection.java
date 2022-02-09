package com.deu.synabro.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Inspection {
    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column
    private Long user_id;

    @Column
    private String title;

    @Column
    private String description;

    @CreatedDate
    @Column(name = "created_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "updated_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedDate;

    @Column(name = "ended_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endedDate;

    public Inspection(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Inspection(String title, String description, LocalDateTime endedDate) {
        this.title = title;
        this.description = description;
        this.endedDate = endedDate;
    }

    @Builder
    public Inspection(Long idx, Long user_id, String title, String description, LocalDateTime createdDate, LocalDateTime updatedDate, LocalDateTime endedDate) {
        this.idx = idx;
        this.user_id = user_id;
        this.title = title;
        this.description = description;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.endedDate = endedDate;
    }
}
