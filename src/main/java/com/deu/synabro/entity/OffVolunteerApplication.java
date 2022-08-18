package com.deu.synabro.entity;

import com.deu.synabro.entity.enums.ApplyOption;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name="off_volunteer_application")
@RequiredArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class OffVolunteerApplication extends BaseTime implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "off_volunteer_application_id", columnDefinition = "BINARY(16)")
    private UUID idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "off_volunteer_id")
    @Schema(description = "봉사 uuid")
    private OffVolunteer offVolunteerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    @Schema(description = "봉사 요청자 아이디", example = "제목")
    private Member userId;

    @Column(columnDefinition = "VARCHAR(255)")
    private String name;

    @Column(columnDefinition = "VARCHAR(255)")
    private String password;

    @Column(columnDefinition = "VARCHAR(255)")
    private String phone;

    @Column(columnDefinition = "VARCHAR(255)")
    private String address;

    @Column(columnDefinition = "VARCHAR(255)")
    private String teamGroup;

    @Column(columnDefinition = "VARCHAR(255)")
    private ApplyOption applyOption;

    @Builder
    public OffVolunteerApplication(OffVolunteer offVolunteerId, Member userId, String name, String password, String phone, String teamGroup, ApplyOption applyOption, String address) {
        this.offVolunteerId = offVolunteerId;
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.teamGroup = teamGroup;
        this.applyOption = applyOption;
        this.address = address;
    }
}
