package com.deu.synabro.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Schema(description = "사용자")
@Getter
@RequiredArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Member extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_id")
    @Schema(description = "사용자 계정 ID", nullable = false, example = "468cc9b5-bca2-494d-9f5c-f00a1af81696")
    private UUID idx;

    @Column(unique = true, length = 50)
    @Schema(description = "사용자 이메일", example = "test@test.com")
    private String email;

    @JsonIgnore
    @Column(nullable = false)
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

    @Column(name = "volunteer_time")
    @Schema(description = "사용자 봉사 시간", example = "56")
    private Short volunteerTime;

    @Column(name = "work_number")
    @Schema(description = "사용자 작업물 개수", example = "23")
    private Short workNumber;

    @Column
    @Schema(description = "사용자 경고 누적 횟수", example = "4")
    private Short warning;

    @Column(name = "lost_login_time")
    @Schema(description = "사용자 마지막 로그인 시간", example = "2022-01-030T05:17:22.024")
    private LocalDateTime lastLoginTime;


    @Column(name = "activated")
    private boolean activated;

    @ManyToMany
    @JoinTable(
            name = "member_authority",
            joinColumns = {@JoinColumn(name = "member_id", referencedColumnName = "member_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")}
    )
    private Set<Authority> authorities;

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    @Builder
    public Member(String email, String password, String username, Set<Authority> authorities) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.phone = "";
        this.address = "";
        this.introduction = "";
        this.volunteerTime = 0;
        this.workNumber = 0;
        this.warning = 0;
        this.lastLoginTime = LocalDateTime.now();
        this.authorities = authorities;
    }
}
