package com.deu.synabro.entity;

import com.deu.synabro.http.request.member.MemberPatchRequest;
import lombok.Getter;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
@Entity
public class Member extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_id", columnDefinition = "BINARY(16)")
    private UUID idx;

    @Email
    @Column(columnDefinition = "VARCHAR(50)", unique = true)
    private String email;

    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    private String password;

    @Column(columnDefinition = "VARCHAR(50)", nullable = false)
    private String username;

    @Column(columnDefinition = "VARCHAR(16)")
    private String phone;

    @Column(columnDefinition = "VARCHAR(255)")
    private String address;

    @Column(columnDefinition = "VARCHAR(255)")
    private String introduction;

    @Column(name = "volunteer_time", columnDefinition = "SMALLINT(6)")
    private Short volunteerTime;

    @Column(name = "work_number", columnDefinition = "SMALLINT(6)")
    private Short workNumber;

    @Column(columnDefinition = "SMALLINT(6)")
    private Short warning;

    @Column(name = "lost_login_time", columnDefinition = "DATETIME(6)")
    private LocalDateTime lastLoginTime;

    @Column(columnDefinition = "BIT(1)")
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

    public Member update(MemberPatchRequest request) {
        if(request.getIntroduction() != null) {
            this.introduction = request.getIntroduction();
        }
        if(request.getAddress() != null) {
            this.address = request.getAddress();
        }
        if(request.getUsername() != null) {
            this.email = request.getUsername();
        }
        if(request.getPhoneNumber() != null) {
            this.phone = request.getPhoneNumber();
        }

        return this;
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

    public Member(UUID idx){
        this.idx = idx;
    }
}
