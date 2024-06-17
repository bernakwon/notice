package com.berna.notice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "공지사항 ID", example = "1")
    private Long id;

    @Schema(description = "제목", example = "공지사항 제목")
    private String title;

    @Lob
    @Schema(description = "내용", example = "공지사항 내용")
    private String content;

    @Schema(description = "시작일시", example = "2024-06-16T10:00:00")
    private LocalDateTime startDate;

    @Schema(description = "종료일시", example = "2024-06-17T10:00:00")
    private LocalDateTime endDate;

    @Schema(description = "조회수", example = "0")
    @Column(name = "view_count")
    private int viewCount = 0;

    @Schema(description = "작성자", example = "관리자")
    private String author;

    @Schema(description = "생성일시", example = "2024-06-16T09:00:00")
    @CreationTimestamp
    private LocalDateTime createdDate;


    @Version
    private int version;

    @Schema(description = "첨부파일 목록")
    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<NoticeAttachment> attachments;

}