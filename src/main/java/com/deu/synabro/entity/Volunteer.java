package com.deu.synabro.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name="volunteer")
@EntityListeners(AuditingEntityListener.class)
public class Volunteer extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "volunteer_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name="user_id")
    private String userId;

    @Column
    private String title;

    @Column
    private String description;

    @Column(name = "ended_date")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endedDate;

    @Builder
    public Volunteer(String userId, String title, String description, LocalDateTime endedDate) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.endedDate = endedDate;
    }
}
