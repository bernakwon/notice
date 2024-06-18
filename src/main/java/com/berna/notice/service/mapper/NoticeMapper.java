package com.berna.notice.service.mapper;


import com.berna.notice.dto.NoticeDto;
import com.berna.notice.dto.NoticeResponseDto;
import com.berna.notice.model.Notice;
import com.berna.notice.model.NoticeAttachment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public Notice toEntity(NoticeDto dto) {
        Notice notice = new Notice();
        notice.setId(dto.getId());
        Optional.ofNullable(dto.getTitle()).ifPresent(notice::setTitle);
        Optional.ofNullable(dto.getContent()).ifPresent(notice::setContent);
        Optional.ofNullable(dto.getAuthor()).ifPresent(notice::setAuthor);

        // startDate 설정
        Optional.ofNullable(dto.getStartDate())
                .map(LocalDateTime::parse)
                .ifPresent(notice::setStartDate);

        // endDate 설정
        Optional.ofNullable(dto.getEndDate())
                .map(LocalDateTime::parse)
                .ifPresent(notice::setEndDate);



        return notice;
    }

    public void updateEntity(NoticeDto dto, Notice notice) {
        Optional.ofNullable(dto.getTitle()).ifPresent(notice::setTitle);
        Optional.ofNullable(dto.getContent()).ifPresent(notice::setContent);
        Optional.ofNullable(dto.getAuthor()).ifPresent(notice::setAuthor);

        // startDate 설정
        Optional.ofNullable(dto.getStartDate())
                .map(LocalDateTime::parse)
                .ifPresent(notice::setStartDate);

        // endDate 설정
        Optional.ofNullable(dto.getEndDate())
                .map(LocalDateTime::parse)
                .ifPresent(notice::setEndDate);


    }



}