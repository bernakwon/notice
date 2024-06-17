package com.berna.notice.service;

import com.berna.notice.model.NoticeAttachment;
import com.berna.notice.service.mapper.NoticeMapper;
import com.berna.notice.repository.NoticeRepository;
import com.berna.notice.dto.NoticeDto;
import com.berna.notice.dto.NoticeResponseDto;
import com.berna.notice.model.Notice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;

    private final NoticeMapper noticeMapper;

    private final String fileStorageLocation = "C:\\fileStorage\\notice";

    @Transactional(readOnly = true)
    public List<NoticeResponseDto> getAllNotices() {
        return noticeRepository.findAll().stream()
                .map(noticeMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public NoticeResponseDto getNoticeById(Long id) {

        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Notice not found with id: " + id));

        notice.setViewCount(notice.getViewCount() + 1);
        noticeRepository.save(notice); // 조회수 증가 후 저장
        return noticeMapper.toResponseDto(notice);
    }

    @Transactional
    public Notice saveOrUpdateNotice(NoticeDto noticeDto) {

        Optional<Notice> targetNotice = noticeRepository.findById(noticeDto.getId());
        if(targetNotice.isPresent()){
            Long targetNoticeId = targetNotice.get().getId();
            noticeDto.setId(targetNoticeId);
        }

        Notice notice = noticeMapper.toEntity(noticeDto);;
        saveAttachments(notice);
        return noticeRepository.save(notice);
    }


    @Transactional
    public void deleteNoticeById(Long id) {
        Optional<Notice> noticeOptional = noticeRepository.findById(id);
        if (noticeOptional.isPresent()) {
            Notice notice = noticeOptional.get();
            deleteAttachments(notice.getAttachments());
            noticeRepository.delete(notice);
        } else {
            throw new RuntimeException("공지사항을 찾을 수 없습니다.");
        }
    }
    private void saveAttachments(Notice notice) {
        for (NoticeAttachment attachment : notice.getAttachments()) {
            try {
                byte[] fileData = attachment.getData();
                Path filePath = Paths.get(fileStorageLocation, attachment.getFileName());
                Files.write(filePath, fileData);
                attachment.setFilePath(filePath.toString());
            } catch (IOException e) {
                throw new RuntimeException("파일 저장 실패", e);
            }
        }
    }
    private void deleteAttachments(List<NoticeAttachment> attachments) {
        for (NoticeAttachment attachment : attachments) {
            try {
                Files.deleteIfExists(Paths.get(attachment.getFilePath()));
            } catch (IOException e) {
                throw new RuntimeException("파일 삭제 실패", e);
            }
        }
    }

}
