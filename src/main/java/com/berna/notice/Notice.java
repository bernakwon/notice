package com.berna.notice;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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

    //조회수 증가
    @Transactional
    public void increaseViewCount() {
        this.viewCount++;
    }
}