package com.berna.notice;


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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


//실제 파일 저장 및 삭제 테스트
@ExtendWith(MockitoExtension.class)
public class NoticeSaveDeleteAttachFileTest {
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

        NoticeAttachmentDto attachmentDto = new NoticeAttachmentDto();
        attachmentDto.setFileName("test.txt");
        attachmentDto.setFileType("text/plain");
        attachmentDto.setData("some text".getBytes());
        noticeDto.setAttachments(Collections.singletonList(attachmentDto));

        notice = new Notice();
        notice.setId(1L);
        notice.setTitle("Test Title");
        notice.setContent("Test Content");

        NoticeAttachment attachment = new NoticeAttachment();
        attachment.setFileName("test.txt");
        attachment.setFileType("text/plain");
        attachment.setData("some text".getBytes());
        attachment.setFilePath("C:\\fileStorage\\notice\\");

        notice.setAttachments(Collections.singletonList(attachment));
    }

    @Test
    @DisplayName("첨부파일이 포함된 공지사항 저장 또는 업데이트")
    void saveOrUpdateNotice_ShouldSaveNoticeWithAttachments() throws IOException {
        // given
        given(noticeMapper.toEntity(noticeDto)).willReturn(notice);
        given(noticeRepository.save(any(Notice.class))).willReturn(notice);

        // when
        Notice result = noticeService.saveOrUpdateNotice(noticeDto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Test Title");
        verify(noticeRepository, times(1)).save(any(Notice.class));

        // 파일이 실제로 저장되었는지 확인
        Path filePath = Paths.get("C:\\fileStorage\\notice", "test.txt");
        assertThat(Files.exists(filePath)).isTrue();

        // 테스트 후 파일 삭제
        Files.deleteIfExists(filePath);
    }

    @Test
    @DisplayName("공지사항 삭제 시 첨부파일도 함께 삭제")
    void deleteNoticeById_ShouldDeleteNoticeAndAttachments() throws IOException {
        // given
        Long noticeId = 1L;

        NoticeAttachment attachment = new NoticeAttachment();
        attachment.setFileName("test.txt");
        attachment.setFilePath("C:\\fileStorage\\notice\\test.txt");

        notice.setAttachments(Collections.singletonList(attachment));

        Path filePath = Paths.get("C:\\fileStorage\\notice", "test.txt");
        Files.createFile(filePath); // 테스트 파일 생성

        given(noticeRepository.findById(noticeId)).willReturn(Optional.of(notice));

        // when
        noticeService.deleteNoticeById(noticeId);

        // then
        verify(noticeRepository, times(1)).findById(noticeId);
        verify(noticeRepository, times(1)).delete(notice);

        // 파일이 실제로 삭제되었는지 확인
        assertThat(Files.exists(filePath)).isFalse();
    }
}