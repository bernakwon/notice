package com.berna.notice;

import com.berna.notice.dto.NoticeDto;
import com.berna.notice.model.Notice;
import com.berna.notice.repository.NoticeRepository;
import com.berna.notice.service.NoticeService;
import com.berna.notice.service.mapper.NoticeMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.OptimisticLockingFailureException;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class NoticeOptimisticLockTest {

    @Mock
    private NoticeMapper noticeMapper;

    @Mock
    private NoticeRepository noticeRepository;

    @InjectMocks
    private NoticeService noticeService;

    private NoticeDto noticeDto;
    private Notice notice;

    @BeforeEach
    void setUp() {
        noticeDto = new NoticeDto();
        noticeDto.setId(1L);
        noticeDto.setTitle("Test Title");
        noticeDto.setContent("Test Content");
        noticeDto.setAttachments(Collections.emptyList());

        notice = new Notice();
        notice.setId(1L);
        notice.setTitle("Test Title");
        notice.setContent("Test Content");
        notice.setAttachments(Collections.emptyList());
        notice.setVersion(0);
    }

    @Test
    @DisplayName("동시성 업데이트 - OptimisticLockException 발생")
    void saveOrUpdateNotice_ShouldThrowOptimisticLockException() {
        // given
        given(noticeRepository.findById(noticeDto.getId())).willReturn(Optional.of(notice));
        given(noticeMapper.toEntity(noticeDto)).willReturn(notice);

        // 동시성 문제를 시뮬레이션하기 위해 버전을 수동으로 증가시킴
        Notice concurrentNotice = new Notice();
        concurrentNotice.setId(1L);
        concurrentNotice.setTitle("Test Title");
        concurrentNotice.setContent("Test Content");
        concurrentNotice.setAttachments(Collections.emptyList());
        concurrentNotice.setVersion(1);

        // 동시성 문제 시뮬레이션을 위해 저장 시 OptimisticLockingFailureException 발생시키기
        given(noticeRepository.save(any(Notice.class))).willThrow(new OptimisticLockingFailureException("동시 업데이트로 인해 저장을 실패했습니다."));

        // when & then
        assertThatThrownBy(() -> noticeService.saveOrUpdateNotice(noticeDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("동시 업데이트로 인해 저장을 실패했습니다.");

        then(noticeRepository).should(times(1)).findById(noticeDto.getId());
        then(noticeMapper).should(times(1)).toEntity(noticeDto);
        then(noticeRepository).should(times(1)).save(notice);
    }
}