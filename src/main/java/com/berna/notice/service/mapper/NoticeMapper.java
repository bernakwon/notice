package com.berna.notice.service.mapper;

import com.berna.notice.dto.NoticeDto;
import com.berna.notice.dto.NoticeResponseDto;
import com.berna.notice.model.Notice;
import com.berna.notice.model.NoticeAttachment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
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
        notice.setId(dto.getId());
        notice.setTitle(dto.getTitle());
        notice.setContent(dto.getContent());
        notice.setStartDate(LocalDateTime.parse(dto.getStartDate()));
        notice.setEndDate(LocalDateTime.parse(dto.getEndDate()));
        List<NoticeAttachment> attachments = Collections.emptyList();
        if (dto.getAttachments() != null) {
            attachments = dto.getAttachments().stream()
                    .map(file -> {
                        NoticeAttachment attachment = new NoticeAttachment();
                        attachment.setFileName(file.getFileName());
                        attachment.setFileType(file.getFileType());
                        attachment.setData(file.getData());
                        attachment.setNotice(notice); // Notice 엔티티와 연결
                        return attachment;
                    }).collect(Collectors.toList());
        }

        notice.setAttachments(attachments);

        notice.setAttachments(attachments);
        return notice;
    }





}