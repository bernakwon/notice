package com.berna.notice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class NoticeAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "첨부 파일 ID", example = "1")
    private Long id;

    @Schema(description = "파일 이름", example = "example.pdf")
    private String fileName;

    @Schema(description = "파일 타입", example = "application/pdf")
    private String fileType;

    @Schema(description = "파일 경로", example = "D:/example/")
    private String filePath;

    @Schema(description = "파일 데이터")
    private byte[] data;

    @Schema(description = "공지사항 ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id")
    private Notice notice;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;
}