package com.berna.notice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class NoticeAttachmentDto {

    @Schema(description = "파일 ID", example = "1")
    private Long id;

    @Schema(description = "파일 이름", example = "example.pdf")
    private String fileName;

    @Schema(description = "파일 타입", example = "application/pdf")
    private String fileType;

    @Schema(description = "파일 데이터")
    private byte[] data;


}