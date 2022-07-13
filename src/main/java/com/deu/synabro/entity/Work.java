package com.deu.synabro.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 봉사 요청을 담는 Entity
 *
 * @author tkfdkskarl56
 * @since 1.0
 */
@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name="work")
@EntityListeners(AuditingEntityListener.class)
public class Work extends BaseTime implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "work_id", columnDefinition = "BINARY(16)")
    @Schema(description = "고유번호", example = "8857ba20-2cb7-407e-908c-b333cf1257c5")
    private UUID idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    @Schema(description = "봉사 요청자 아이디", example = "제목")
    private Member userId;

    @Column(name="title")
    @Schema(description = "게시판 제목", example = "제목")
    private String title;

    @Column(name="contents")
    @Schema(description = "게시판 내용", example = "내용")
    private String contents;

    @Column(name = "volunteer_time")
    @Schema(description = "봉사 시간", example = "56")
    private Short volunteerTime;

    @Column(name = "ended_date")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endedDate;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "work")
    private List<Docs> docsList = new ArrayList<>();

    /**
     * Docs에 Work의 UUID값을 넣어주는 메소드 입니다.
     *
     * @param docs 봉사 요청 게시글과 같이 저장할 Docs 객체입니다.
     */
    public void addDocs(Docs docs){
        this.docsList.add(docs);
        docs.setWork(this);
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "work")
    private List<Video> videoList = new ArrayList<>();

    public void addVideo(Video video){
        this.videoList.add(video);
        video.setWork(this);
    }

    @Builder
    public Work(UUID idx, Member userId, String title, String contents, Short volunteerTime, LocalDateTime endedDate) {
        this.idx = idx;
        this.userId = userId;
        this.title = title;
        this.contents = contents;
        this.volunteerTime = volunteerTime;
        this.endedDate = endedDate;
    }
}
