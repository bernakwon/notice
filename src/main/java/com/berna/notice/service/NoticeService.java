package com.berna.notice.service;

import com.berna.global.error.CommonException;
import com.berna.global.error.ErrorCode;
import com.berna.notice.model.NoticeAttachment;
import com.berna.notice.repository.NoticeAttachmentRepository;
import com.berna.notice.service.mapper.NoticeMapper;
import com.berna.notice.repository.NoticeRepository;
import com.berna.notice.dto.NoticeDto;
import com.berna.notice.dto.NoticeResponseDto;
import com.berna.notice.model.Notice;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
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
import java.util.*;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;

    private final NoticeAttachmentRepository noticeAttachmentRepository;

    private final NoticeMapper noticeMapper;

    private final String fileStorageLocation = "C:\\fileStorage\\notice\\";


    public Page<Notice> getAllNotices(PageRequest pageRequest) {
        return noticeRepository.findAll(pageRequest);
    }

    @Transactional
    public NoticeResponseDto getNoticeById(Long id) {

        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.NOTICE_NOT_FOUND));

        notice.setViewCount(notice.getViewCount() + 1);


        noticeRepository.save(notice); // 조회수 증가 후 저장
        List<NoticeAttachment> attachments = noticeAttachmentRepository.findByNoticeId(id);

        notice.setAttachments(attachments);
        return noticeMapper.toResponseDto(notice);
    }

    @Transactional
    public Notice saveOrUpdateNotice(NoticeDto noticeDto) {
        Notice notice;

        // 공지사항 ID가 존재하면 기존 공지사항을 업데이트
        if (noticeDto.getId() != null) {
            notice = noticeRepository.findById(noticeDto.getId())
                    .orElseThrow(() -> new CommonException(ErrorCode.NOTICE_NOT_FOUND));
            noticeMapper.updateEntity(noticeDto, notice);

        } else {
            notice = noticeMapper.toEntity(noticeDto);
        }
        List<NoticeAttachment> attachmentsToDelete = saveAttachments(notice, noticeDto.getAttachments());
        try {
            Notice savedNotice = noticeRepository.save(notice);
            // 데이터베이스에서 삭제된 첨부 파일 삭제
            if (!attachmentsToDelete.isEmpty()) {
                noticeAttachmentRepository.deleteAll(attachmentsToDelete);
            }
            return savedNotice;
        } catch (OptimisticLockException e) {
            throw new CommonException(ErrorCode.SIMULTANEOUS_UPDATE);
        }

    }


    @Transactional
    public void deleteNoticeById(Long id) {
        Optional<Notice> noticeOptional = noticeRepository.findById(id);
        if (noticeOptional.isPresent()) {
            Notice notice = noticeOptional.get();
            if (!notice.getAttachments().isEmpty()) {
                deleteAttachments(notice.getAttachments());
                noticeAttachmentRepository.deleteAll(notice.getAttachments());
            }
            noticeRepository.delete(notice);
        } else {
            throw new CommonException(ErrorCode.NOTICE_NOT_FOUND);
        }
    }

    @Transactional
    public List<NoticeAttachment> saveAttachments(Notice notice, List<MultipartFile> attachments) {
        List<NoticeAttachment> newAttachments = new ArrayList<>();
        if (attachments != null && !attachments.isEmpty()) {

            newAttachments = attachments.stream().map(file -> {
                try {
                    String fileName =   UUID.nameUUIDFromBytes(file.getOriginalFilename().getBytes()).toString();
                    String extension = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf("."));
                    Path filePath = Paths.get(fileStorageLocation + fileName+extension);
                    Files.createDirectories(filePath.getParent());
                    Files.write(filePath, file.getBytes());

                    NoticeAttachment attachment = new NoticeAttachment();
                    attachment.setFileName(fileName);
                    attachment.setOriginalFileName(file.getOriginalFilename());
                    attachment.setFileType(file.getContentType());
                    attachment.setFilePath(filePath.toString());
                    attachment.setData(file.getBytes());
                    attachment.setNotice(notice);
                    return attachment;
                } catch (IOException e) {
                    throw new CommonException(ErrorCode.FAIL_FILE_SAVE);
                }
            }).collect(Collectors.toList());


        }
        // 기존 첨부 파일과 새로운 첨부 파일을 비교하여 삭제된 파일들을 제거
        List<NoticeAttachment> currentAttachments = notice.getAttachments();
        List<NoticeAttachment> attachmentsToDelete = new ArrayList<>();
        if (currentAttachments != null) {
            List<NoticeAttachment> finalNewAttachments = newAttachments;
            attachmentsToDelete = currentAttachments.stream()
                    .filter(existingAttachment ->
                            finalNewAttachments.stream().noneMatch(newAttachment ->
                                    newAttachment.getFileName().equals(existingAttachment.getFileName())))
                    .collect(Collectors.toList());
            currentAttachments.removeAll(attachmentsToDelete);
        }
        // 새로운 첨부 파일 리스트 설정
        notice.setAttachments(newAttachments);
        return attachmentsToDelete;
    }

    @Transactional
    void deleteAttachments(List<NoticeAttachment> attachments) {
        for (NoticeAttachment attachment : attachments) {
            try {
                Files.deleteIfExists(Paths.get(attachment.getFilePath()));
            } catch (IOException e) {
                throw new CommonException(ErrorCode.FAIL_FILE_DELETE);
            }
        }
    }

}
