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

import java.util.Collections;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NoticeSaveTest {

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
        noticeDto.setId(1L); // 업데이트 케이스를 가정하여 ID 설정
        noticeDto.setTitle("Test Title");
        noticeDto.setContent("Test Content");
        noticeDto.setAttachments(Collections.emptyList()); // 첨부파일은 없음

        notice = new Notice();
        notice.setId(1L);
        notice.setTitle("Test Title");
        notice.setContent("Test Content");
        notice.setAttachments(Collections.emptyList());
    }

    @Test
    @DisplayName("기존 공지사항 업데이트")
    void saveOrUpdateNotice_ShouldUpdateExistingNotice() {
        // given
        given(noticeRepository.findById(noticeDto.getId())).willReturn(Optional.of(notice));
        given(noticeMapper.toEntity(noticeDto)).willReturn(notice);
        given(noticeRepository.save(any(Notice.class))).willReturn(notice);

        // when
        Notice result = noticeService.saveOrUpdateNotice(noticeDto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(noticeDto.getId());
        assertThat(result.getTitle()).isEqualTo("Test Title");
        assertThat(result.getContent()).isEqualTo("Test Content");

        then(noticeRepository).should(times(1)).findById(noticeDto.getId());
        then(noticeMapper).should(times(1)).toEntity(noticeDto);
        then(noticeRepository).should(times(1)).save(notice);
    }

    @Test
    @DisplayName("새로운 공지사항 저장")
    void saveOrUpdateNotice_ShouldSaveNewNotice() {
        // given
        NoticeDto newNoticeDto = new NoticeDto();
        newNoticeDto.setTitle("New Test Title");
        newNoticeDto.setContent("New Test Content");
        newNoticeDto.setAttachments(Collections.emptyList());

        Notice newNotice = new Notice();
        newNotice.setTitle("New Test Title");
        newNotice.setContent("New Test Content");
        newNotice.setAttachments(Collections.emptyList());

        given(noticeRepository.findById(newNoticeDto.getId())).willReturn(Optional.empty());
        given(noticeMapper.toEntity(newNoticeDto)).willReturn(newNotice);
        given(noticeRepository.save(any(Notice.class))).willReturn(newNotice);

        // when
        Notice result = noticeService.saveOrUpdateNotice(newNoticeDto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("New Test Title");
        assertThat(result.getContent()).isEqualTo("New Test Content");

        then(noticeRepository).should(times(1)).findById(newNoticeDto.getId());
        then(noticeMapper).should(times(1)).toEntity(newNoticeDto);
        then(noticeRepository).should(times(1)).save(newNotice);
    }
}

