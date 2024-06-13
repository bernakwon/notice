package com.berna.notice;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
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
        notice.setTitle(dto.getTitle());
        notice.setContent(dto.getContent());
        notice.setStartDate(dto.getStartDate());
        notice.setEndDate(dto.getEndDate());

        List<NoticeAttachment> attachments = dto.getAttachments().stream()
                .map(file -> {
                    try {
                        NoticeAttachment attachment = new NoticeAttachment();
                        attachment.setFileName(file.getOriginalFilename());
                        attachment.setFileType(file.getContentType());
                        attachment.setData(file.getBytes());
                        return attachment;
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to store file", e);
                    }
                }).collect(Collectors.toList());

        notice.setAttachments(attachments);
        return notice;
    }

    public NoticeDto toDto(Notice entity) {
        NoticeDto dto = new NoticeDto();
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());

        return dto;
    }



}