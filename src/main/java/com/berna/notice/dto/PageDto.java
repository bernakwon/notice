package com.berna.notice.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "페이지 응답 DTO")
public class PageDto<T> {
    @Schema(description = "현재 페이지 번호")
    private int currentPage;

    @Schema(description = "총 페이지 수")
    private int totalPages;

    @Schema(description = "총 요소 수")
    private long totalElements;

    @Schema(description = "페이지당 요소 수")
    private int pageSize;

    @Schema(description = "페이지의 내용 목록")
    private List<T> content;

    public PageDto(int currentPage, int totalPages, long totalElements, int pageSize, List<T> content) {
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.pageSize = pageSize;
        this.content = content;
    }
}