package com.berna.notice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Data
public class NoticeDto {
    //아이디
    private Long id;

    @Schema(description = "제목", example = "공지사항 제목")
    private String title;

    @Schema(description = "내용", example = "공지사항 내용")
    private String content;

    @Schema(description = "시작일시", example = "2024-06-16T10:00:00")
    private String startDate;

    @Schema(description = "종료일시", example = "2024-06-17T10:00:00")
    private String endDate;

    @Schema(description = "작성자", example = "관리자")
    private String author;

   //첨부파일 목록
    private List<MultipartFile> attachments;


}