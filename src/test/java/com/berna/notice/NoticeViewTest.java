package com.berna.notice;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Optional;

import com.berna.notice.dto.NoticeDto;
import com.berna.notice.dto.NoticeResponseDto;
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

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NoticeViewTest {

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

        notice = new Notice();
        notice.setId(1L);
        notice.setTitle("Test Title");
        notice.setContent("Test Content");
        notice.setStartDate(LocalDateTime.now());
        notice.setEndDate(LocalDateTime.now().plusDays(7));
        notice.setViewCount(0); // 초기 조회수 설정
    }

    @Test
    @DisplayName("공지사항 조회 시 조회수 증가")
    void getNoticeById_ShouldIncreaseViewCount() {
        // given
        given(noticeRepository.findById(noticeDto.getId())).willReturn(Optional.of(notice));

        NoticeResponseDto responseDto = new NoticeResponseDto();
        responseDto.setId(notice.getId());
        responseDto.setTitle(notice.getTitle());
        responseDto.setContent(notice.getContent());
        responseDto.setViewCount(notice.getViewCount()); // 조회수 설정

        given(noticeMapper.toResponseDto(notice)).willReturn(responseDto);

        // when
        NoticeResponseDto result = noticeService.getNoticeById(noticeDto.getId());

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(notice.getId());
        assertThat(result.getTitle()).isEqualTo("Test Title");
        assertThat(result.getContent()).isEqualTo("Test Content");

        verify(noticeRepository, times(1)).findById(noticeDto.getId());
        verify(noticeMapper, times(1)).toResponseDto(notice);
    }
}


