package com.berna.notice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import java.util.Optional;
import java.util.Collections;

import com.berna.notice.dto.NoticeAttachmentDto;
import com.berna.notice.dto.NoticeDto;
import com.berna.notice.model.Notice;
import com.berna.notice.model.NoticeAttachment;
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

@ExtendWith(MockitoExtension.class)
public class NoticeSaveAttachFileTest {

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
        // NoticeDto에 첨부파일 설정
        NoticeAttachmentDto attachmentDto = new NoticeAttachmentDto();
        attachmentDto.setFileName("testFile.txt");
        attachmentDto.setFileType("text/plain");
        attachmentDto.setData("test data".getBytes());

        noticeDto = new NoticeDto();
        noticeDto.setId(1L); // 업데이트 케이스를 가정하여 ID 설정
        noticeDto.setTitle("Test Title");
        noticeDto.setContent("Test Content");
        noticeDto.setAttachments(Collections.singletonList(attachmentDto));

        // Notice 엔티티에 첨부파일 설정
        NoticeAttachment attachment = new NoticeAttachment();
        attachment.setFileName("testFile.txt");
        attachment.setFileType("text/plain");
        attachment.setData("test data".getBytes());

        notice = new Notice();
        notice.setId(1L);
        notice.setTitle("Test Title");
        notice.setContent("Test Content");
        notice.setAttachments(Collections.singletonList(attachment));
    }

    @Test
    @DisplayName("기존 공지사항 업데이트 시 첨부파일과 함께 저장")
    void saveOrUpdateNotice_ShouldUpdateExistingNoticeWithAttachments() {
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
        assertThat(result.getAttachments()).hasSize(1);
        NoticeAttachment resultAttachment = result.getAttachments().get(0);
        assertThat(resultAttachment.getFileName()).isEqualTo("testFile.txt");
        assertThat(resultAttachment.getFileType()).isEqualTo("text/plain");
        assertThat(resultAttachment.getData()).isEqualTo("test data".getBytes());

        then(noticeRepository).should(times(1)).findById(noticeDto.getId());
        then(noticeMapper).should(times(1)).toEntity(noticeDto);
        then(noticeRepository).should(times(1)).save(notice);
    }

    @Test
    @DisplayName("새로운 공지사항 저장 시 첨부파일과 함께 저장")
    void saveOrUpdateNotice_ShouldSaveNewNoticeWithAttachments() {
        // given
        NoticeAttachmentDto newAttachmentDto = new NoticeAttachmentDto();
        newAttachmentDto.setFileName("newTestFile.txt");
        newAttachmentDto.setFileType("text/plain");
        newAttachmentDto.setData("new test data".getBytes());

        NoticeDto newNoticeDto = new NoticeDto();
        newNoticeDto.setTitle("New Test Title");
        newNoticeDto.setContent("New Test Content");
        newNoticeDto.setAttachments(Collections.singletonList(newAttachmentDto));

        NoticeAttachment newAttachment = new NoticeAttachment();
        newAttachment.setFileName("newTestFile.txt");
        newAttachment.setFileType("text/plain");
        newAttachment.setData("new test data".getBytes());

        Notice newNotice = new Notice();
        newNotice.setTitle("New Test Title");
        newNotice.setContent("New Test Content");
        newNotice.setAttachments(Collections.singletonList(newAttachment));

        given(noticeRepository.findById(newNoticeDto.getId())).willReturn(Optional.empty());
        given(noticeMapper.toEntity(newNoticeDto)).willReturn(newNotice);
        given(noticeRepository.save(any(Notice.class))).willReturn(newNotice);

        // when
        Notice result = noticeService.saveOrUpdateNotice(newNoticeDto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("New Test Title");
        assertThat(result.getContent()).isEqualTo("New Test Content");
        assertThat(result.getAttachments()).hasSize(1);
        NoticeAttachment resultAttachment = result.getAttachments().get(0);
        assertThat(resultAttachment.getFileName()).isEqualTo("newTestFile.txt");
        assertThat(resultAttachment.getFileType()).isEqualTo("text/plain");
        assertThat(resultAttachment.getData()).isEqualTo("new test data".getBytes());

        then(noticeRepository).should(times(1)).findById(newNoticeDto.getId());
        then(noticeMapper).should(times(1)).toEntity(newNoticeDto);
        then(noticeRepository).should(times(1)).save(newNotice);
    }
}