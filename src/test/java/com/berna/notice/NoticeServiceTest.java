package com.berna.notice;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class NoticeServiceTest {
    @InjectMocks
    private NoticeService noticeService;
    @Mock
    private NoticeRepository noticeRepository;

    @Mock
    private NoticeMapper noticeMapper;



    Notice notice;
    NoticeDto noticeDto;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        notice = new Notice();
        notice.setId(1L);
        notice.setTitle("Test Title");
        notice.setContent("Test Content");
        notice.setViewCount(0); // 초기 조회수 설정

        noticeDto = new NoticeDto();
        noticeDto.setTitle("Test Title");
        noticeDto.setContent("Test Content");

    }

    @Test
    void getNoticeById_ShouldIncreaseViewCount() {
        // given
        given(noticeRepository.findById(1L)).willReturn(Optional.ofNullable(notice));
        given(noticeMapper.toResponseDto(notice)).willReturn(new NoticeResponseDto());

        // when
        NoticeResponseDto result = noticeService.getNoticeById(1L);

        // then
        assertThat(result.getViewCount()).isEqualTo(1);
        assertThat(result).isNotNull();

        then(noticeRepository).should().findById(1L);
        then(noticeMapper).should().toResponseDto(notice);
        then(noticeRepository).should().save(notice);
    }

    @Test
    void saveOrUpdateNotice_ShouldSaveNotice() {

        // given
        NoticeDto noticeDto = new NoticeDto();
        noticeDto.setTitle("Test Title");
        noticeDto.setContent("Test Content");

        Notice notice = new Notice();
        notice.setId(1L);
        notice.setTitle(noticeDto.getTitle());
        notice.setContent(noticeDto.getContent());


        given(noticeMapper.toEntity(noticeDto)).willReturn(notice);
        given(noticeRepository.save(any(Notice.class))).willReturn(notice);

        // when
        Notice result = noticeService.saveOrUpdateNotice(noticeDto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Test Title");
        assertThat(result.getContent()).isEqualTo("Test Content");
        assertThat(result.getAttachments()).isNotEmpty(); // Check if attachments are not empty

        verify(noticeMapper, times(1)).toEntity(noticeDto);
        verify(noticeRepository, times(1)).save(any(Notice.class));
    }
    }


