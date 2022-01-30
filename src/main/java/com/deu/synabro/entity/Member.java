package com.deu.synabro.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "사용자")
@Getter
@RequiredArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_id")
    private UUID idx;

    @Column(unique = true, length = 50)
    private String email;

    @JsonIgnore
    @Column(nullable = false, length = 50)
    private String password;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(length = 16)
    private String phone;

    @Column
    private String address;

    @Column
    private String introduction;

    @Column
    private Short volunteer_time;

    @Column
    private Short work_number;

    @Column
    private Short warning;

    @Column
    private LocalDateTime last_login_time;

    @CreatedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime created_date;

    @LastModifiedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updated_date;

    @Builder
    public Member(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.phone = "";
        this.address = "";
        this.introduction = "";
        this.volunteer_time = 0;
        this.work_number = 0;
        this.warning = 0;
        this.last_login_time = LocalDateTime.now();
    }
}
