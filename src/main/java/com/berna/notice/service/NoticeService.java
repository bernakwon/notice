package com.berna.notice.service;

import com.berna.notice.model.NoticeAttachment;
import com.berna.notice.service.mapper.NoticeMapper;
import com.berna.notice.repository.NoticeRepository;
import com.berna.notice.dto.NoticeDto;
import com.berna.notice.dto.NoticeResponseDto;
import com.berna.notice.model.Notice;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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


    public Page<Notice> getAllNotices(PageRequest pageRequest) {
        return noticeRepository.findAll(pageRequest);
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
        saveAttachments(notice, noticeDto.getAttachments());

        try {
            return noticeRepository.save(notice);
        } catch (OptimisticLockException e) {
            throw new RuntimeException("동시 업데이트로 인해 저장을 실패했습니다.", e);
        }
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
    private void saveAttachments(Notice notice, List<MultipartFile> attachments) {
        if (attachments != null && !attachments.isEmpty()) {
            // 파일 저장 경로 설정

            // 각 파일을 저장하고 NoticeAttachment 생성 후 연결
            List<NoticeAttachment> noticeAttachments = attachments.stream().map(file -> {
                try {
                    Path filePath = Paths.get(fileStorageLocation + file.getOriginalFilename());
                    Files.write(filePath, file.getBytes());

                    NoticeAttachment attachment = new NoticeAttachment();
                    attachment.setFileName(file.getOriginalFilename());
                    attachment.setFileType(file.getContentType());
                    attachment.setData(file.getBytes());
                    attachment.setNotice(notice);
                    return attachment;
                } catch (IOException e) {
                    throw new RuntimeException("파일 저장 실패", e);
                }
            }).collect(Collectors.toList());

            notice.setAttachments(noticeAttachments);
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
