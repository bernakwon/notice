package com.berna.notice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;


@Data
public class NoticeDto {
    @Schema(description = "공지사항 ID", example = "1")
    private Long id;

    @Schema(description = "제목", example = "공지사항 제목")
    private String title;

    @Schema(description = "내용", example = "공지사항 내용")
    private String content;

    @Schema(description = "시작일시", example = "2024-06-16T10:00:00")
    private String startDate;

    @Schema(description = "종료일시", example = "2024-06-17T10:00:00")
    private String endDate;

    @Schema(description = "첨부파일 목록")
    private List<NoticeAttachmentDto> attachments;


}