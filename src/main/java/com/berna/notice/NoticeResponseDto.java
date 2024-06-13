package com.berna.notice;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NoticeResponseDto {
    private String title;
    private String content;
    private LocalDateTime createdDate;
    private int viewCount;
    private String author;
}
