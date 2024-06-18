package com.berna.notice;

import com.berna.global.error.CommonException;
import com.berna.global.error.ErrorCode;
import com.berna.notice.dto.NoticeDto;
import com.berna.notice.model.Notice;
import com.berna.notice.repository.NoticeAttachmentRepository;
import com.berna.notice.repository.NoticeRepository;
import com.berna.notice.service.NoticeService;
import com.berna.notice.service.mapper.NoticeMapper;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
public class NoticeOptimisticLockTest {
    @Mock
    private NoticeRepository noticeRepository;

    @Mock
    private NoticeAttachmentRepository noticeAttachmentRepository;

    @Mock
    private NoticeMapper noticeMapper;

    @InjectMocks
    private NoticeService noticeService;

    @Test
    @Transactional
    public void testOptimisticLockException() {
        NoticeDto noticeDto = new NoticeDto();
        noticeDto.setId(1L);
        noticeDto.setTitle("Test Title");
        noticeDto.setContent("Test Content");

        Notice existingNotice = new Notice();
        existingNotice.setId(1L);
        existingNotice.setTitle("Old Title");
        existingNotice.setContent("Old Content");
        existingNotice.setVersion(1);  // 초기 버전 설정

        when(noticeRepository.findById(1L)).thenReturn(Optional.of(existingNotice));
        doNothing().when(noticeMapper).updateEntity(noticeDto, existingNotice);
        when(noticeRepository.save(any(Notice.class))).thenThrow(new OptimisticLockException("낙관적 잠금 예외 발생"));

        try {
            noticeService.saveOrUpdateNotice(noticeDto);
        } catch (CommonException e) {
            if (e.getErrorCode().equals(ErrorCode.SIMULTANEOUS_UPDATE)) {
                // 테스트 성공
                return;
            } else {
                throw new AssertionError("OptimisticLockException이 발생하지 않았습니다.", e);
            }
        }

        throw new AssertionError("OptimisticLockException이 발생하지 않았습니다.");
    }
}