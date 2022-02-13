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
import java.util.Set;
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
    @Schema(description = "사용자 계정 ID", nullable = false, example = "468cc9b5-bca2-494d-9f5c-f00a1af81696")
    private UUID idx;

    @Column(unique = true, length = 50)
    @Schema(description = "사용자 이메일", example = "test@test.com")
    private String email;

    @JsonIgnore
    @Column(nullable = false, length = 50)
    @Schema(description = "사용자 암호", example = "testPassword")
    private String password;

    @Column(unique = true, nullable = false)
    @Schema(description = "사용자 이름", example = "testUsername")
    private String username;

    @Column(length = 16)
    @Schema(description = "사용자 휴대폰 번호", example = "010-1234-5678")
    private String phone;

    @Column
    @Schema(description = "사용자 주소", example = "00시 00구 000 123")
    private String address;

    @Column
    @Schema(description = "사용자 자기소개 글", example = "test 사용자의 자기소개글입니다.")
    private String introduction;

    @Column
    @Schema(description = "사용자 봉사 시간", example = "56")
    private Short volunteer_time;

    @Column
    @Schema(description = "사용자 작업물 개수", example = "23")
    private Short work_number;

    @Column
    @Schema(description = "사용자 경고 누적 횟수", example = "4")
    private Short warning;

    @Column
    @Schema(description = "사용자 마지막 로그인 시간", example = "2022-01-030T05:17:22.024")
    private LocalDateTime last_login_time;

    @CreatedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "사용자 계정 생성 시간", example = "2021-11-26T06:32:14.168")
    private LocalDateTime created_date;

    @LastModifiedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "사용자 계정 정보 마지막 수정 시간", example = "2022-01-02T12:23:48.753")
    private LocalDateTime updated_date;

    @ManyToMany
    @JoinTable(
            name = "member_authority",
            joinColumns = {@JoinColumn(name = "member_id", referencedColumnName = "member_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")}
    )
    private Set<Authority> authorities;

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
