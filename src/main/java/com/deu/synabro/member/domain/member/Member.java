//package com.deu.synabro.member.domain.member;
//
//import com.deu.synabro.member.domain.BaseTimeEntity;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//import java.time.LocalDateTime;
//
//@Builder
//@Data
//@NoArgsConstructor
//@Entity
//public class Member extends BaseTimeEntity {
//    @Id
//    @Column(unique = true)
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long idx;
//
//    @Column(unique = true, length = 50)
//    private String email;
//
//    @Column(nullable = false, length = 50)
//    private String password;
//
//    @Column(nullable = false)
//    private String username;
//
//    @Column
//    private String phone;
//
//    @Column
//    private String address;
//
//    @Column
//    private String introduction;
//
//    @Column
//    private Integer volunteer_time;
//
//    @Column
//    private Integer work_number;
//
//    @Column
//    private Integer warning;
//
//    @Column
//    private LocalDateTime last_login_time;
//
//    public Member(Long idx, String email, String password, String username) {
//        this.idx = idx;
//        this.email = email;
//        this.password = password;
//        this.username = username;
//        this.phone = "";
//        this.address = "";
//        this.introduction = "";
//        this.volunteer_time = 0;
//        this.work_number = 0;
//        this.warning = 0;
//        this.last_login_time = LocalDateTime.now();
//    }
//
//    public Member(Long idx, String email, String password, String username, String phone, String address,
//                  String introduction, Integer volunteer_time, Integer work_number, Integer warning, LocalDateTime last_login_time) {
//        this.idx = idx;
//        this.email = email;
//        this.password = password;
//        this.username = username;
//        this.phone = phone;
//        this.address = address;
//        this.introduction = introduction;
//        this.volunteer_time = volunteer_time;
//        this.work_number = work_number;
//        this.warning = warning;
//        this.last_login_time = last_login_time;
//    }
//}
