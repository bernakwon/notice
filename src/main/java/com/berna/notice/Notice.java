package com.berna.notice;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    private LocalDateTime startDate;

    private LocalDateTime endDate;


    @Column(name = "view_count")
    private int viewCount;


    private String author;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<NoticeAttachment> attachments;


    //조회수 증가
    public void increaseViewCount() {
        this.viewCount++;
    }
}