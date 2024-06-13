package com.berna.notice;

import org.springframework.stereotype.Component;

@Component
public class NoticeMapper {


    public NoticeResponseDto toResponseDto(Notice notice) {
        NoticeResponseDto dto = new NoticeResponseDto();
        dto.setTitle(notice.getTitle());
        dto.setContent(notice.getContent());
        dto.setCreatedDate(notice.getCreatedDate());
        dto.setViewCount(notice.getViewCount());
        dto.setAuthor(notice.getAuthor());

        return dto;
    }




}