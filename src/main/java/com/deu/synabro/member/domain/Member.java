package com.deu.synabro.member.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
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
public class Member {
    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(unique = true, length = 50)
    private String userId;

    @Column(unique = true, length = 50)
    private String email;

    @Column(nullable = false, length = 50)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(unique = true, nullable = false)
    private String username;

    @Column
    private String phone;

    @Column
    private String address;

    @Column
    private String introduction;

    @Column
    private Integer volunteer_time;

    @Column
    private Integer work_number;

    @Column
    private Integer warning;

    @Column
    private LocalDateTime last_login_time;

    @CreatedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime created_date;

    @LastModifiedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updated_date;

    public Member(String userId, String email, String password, String username) {
        this.userId = userId;
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

    @Builder
    public Member(Long idx, String email, String password, String username, String phone, String address,
                  String introduction, Integer volunteer_time, Integer work_number, Integer warning,
                  LocalDateTime last_login_time, LocalDateTime created_date, LocalDateTime updated_date) {
        this.idx = idx;
        this.email = email;
        this.password = password;
        this.username = username;
        this.phone = phone;
        this.address = address;
        this.introduction = introduction;
        this.volunteer_time = volunteer_time;
        this.work_number = work_number;
        this.warning = warning;
        this.last_login_time = last_login_time;
    }
}
