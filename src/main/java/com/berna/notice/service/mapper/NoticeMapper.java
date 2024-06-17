package com.berna.notice.service.mapper;


import com.berna.notice.dto.NoticeDto;
import com.berna.notice.dto.NoticeResponseDto;
import com.berna.notice.model.Notice;
import com.berna.notice.model.NoticeAttachment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
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
        List<NoticeAttachment> attachments = Collections.emptyList();
        // 첨부 파일 정보 설정
        if (dto.getAttachments() != null && !dto.getAttachments().isEmpty()) {
           attachments = dto.getAttachments().stream()
                    .map(file -> {
                        try {
                            NoticeAttachment attachment = new NoticeAttachment();
                            attachment.setFileName(file.getOriginalFilename());
                            attachment.setFileType(file.getContentType());
                            attachment.setData(file.getBytes());
                            attachment.setNotice(notice); // Notice와의 관계 설정
                            return attachment;
                        } catch (IOException e) {
                            throw new RuntimeException("파일저장 실패", e);
                        }
                    })
                    .collect(Collectors.toList());
            notice.setAttachments(attachments);
        }



        return notice;
    }


    public NoticeResponseDto toDto(Notice notice) {
        NoticeResponseDto dto = new NoticeResponseDto();
        dto.setId(notice.getId());
        dto.setTitle(notice.getTitle());
        dto.setContent(notice.getContent());
        dto.setViewCount(notice.getViewCount());
        dto.setAuthor(notice.getAuthor());
        dto.setCreatedDate(notice.getCreatedDate());

        return dto;
    }




}