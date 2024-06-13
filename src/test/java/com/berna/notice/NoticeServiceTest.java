package com.berna.notice;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class NoticeServiceTest {

    @Mock
    private NoticeRepository noticeRepository;

    @Mock
    private NoticeMapper noticeMapper;

    @InjectMocks
    private NoticeService noticeService;

    private Notice notice;
    private NoticeDto noticeDto;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        notice = new Notice();
        notice.setId(1L);
        notice.setTitle("Test Title");
        notice.setContent("Test Content");

        noticeDto = new NoticeDto();
        noticeDto.setTitle("Test Title");
        noticeDto.setContent("Test Content");
    }

    @Transactional
    @Test
    void getNoticeById_ShouldIncreaseViewCount() {
        // Mock 데이터 설정
        when(noticeRepository.findById(1L)).thenReturn(Optional.of(notice));
        when(noticeMapper.toResponseDto(notice)).thenReturn(new NoticeResponseDto());

        // 테스트 실행
        NoticeResponseDto result = noticeService.getNoticeById(1L);

        // 조회수가 1 증가했는지 확인
        assertThat(result.getViewCount()).isEqualTo(1);
        // 서비스가 올바른 DTO를 반환하는지 확인
        assertThat(result).isNotNull();
        // Repository 메서드가 정확히 1회 호출되었는지 확인
        verify(noticeRepository, times(1)).findById(1L);
        // Mapper 메서드가 정확히 1회 호출되었는지 확인
        verify(noticeMapper, times(1)).toResponseDto(notice);
    }

}