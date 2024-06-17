package com.berna.notice;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class NoticePagingTest {

    @Mock
    private NoticeRepository noticeRepository;

    @Mock
    private NoticeMapper noticeMapper;

    @InjectMocks
    private NoticeService noticeService;

    private Notice notice;
    private NoticeResponseDto noticeResponseDto;

    @BeforeEach
    void setUp() {
        notice = new Notice();
        notice.setId(1L);
        notice.setTitle("Test Title");
        notice.setContent("Test Content");
        notice.setAttachments(Collections.emptyList());

        noticeResponseDto = new NoticeResponseDto();
        noticeResponseDto.setId(1L);
        noticeResponseDto.setTitle("Test Title");
        noticeResponseDto.setContent("Test Content");

    }

    @Test
    @DisplayName("공지사항 목록 페이징 조회")
    void getAllNotices_ShouldReturnPagedNotices() {
        // given
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Notice> noticePage = new PageImpl<>(Collections.singletonList(notice), pageable, 1);
        given(noticeRepository.findAll(pageable)).willReturn(noticePage);
        given(noticeMapper.toDto(notice)).willReturn(noticeResponseDto);

        // when
        Page<Notice> result = noticeService.getAllNotices(pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("Test Title");

        then(noticeRepository).should(times(1)).findAll(pageable);
    }
}
