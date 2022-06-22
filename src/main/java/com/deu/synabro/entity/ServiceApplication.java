package com.deu.synabro.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name="ServiceApplication")
@RequiredArgsConstructor
public class ServiceApplication extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ServiceApplication_id", columnDefinition = "BINARY(16)")
    private UUID idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    @Schema(description = "봉사 아이디")
    private Service serviceId;

    @Column
    @Schema(description = "신청자 이름", example = "김선웅")
    private String name;

    @Column
    @Schema(description = "신청자 나이", example = "25")
    private Short age;

    @Column
    @Schema(description = "신청자 소속", example = "동의대학교")
    private String group;

    @Column
    @Schema(description = "연락처", example = "010-8921-8709")
    private String phone;


}
