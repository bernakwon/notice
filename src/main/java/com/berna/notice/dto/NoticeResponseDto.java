package com.berna.notice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeResponseDto {

    @Schema(description = "공지사항 ID", example = "1")
    private Long id;

    @Schema(description = "제목", example = "공지사항 제목")
    private String title;

    @Schema(description = "내용", example = "공지사항 내용")
    private String content;

    @Schema(description = "등록일시", example = "2024-06-16T10:00:00")
    private LocalDateTime createdDate;

    @Schema(description = "조회수", example = "10")
    private int viewCount;

    @Schema(description = "작성자", example = "관리자")
    private String author;

    @Schema(description = "첨부파일명 목록")
    private List<String> attachmentNames;

    public NoticeResponseDto(Long id, String title, String content, LocalDateTime createdDate, int viewCount, String author) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.viewCount = viewCount;
        this.author = author;
    }
}
