package com.berna.notice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
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
import org.springframework.mock.web.MockMultipartFile;

//엔티티 매핑 후 1차 테스트
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
        // 테스트용 NoticeDto 객체 생성
        noticeDto = new NoticeDto();
        noticeDto.setId(1L);
        noticeDto.setTitle("Test Title");
        noticeDto.setContent("Test Content");

        // 테스트용 Notice 객체 생성
        notice = new Notice();
        notice.setId(1L);
        notice.setTitle("Test Title");
        notice.setContent("Test Content");
    }

    @Test
    @DisplayName("기존 공지사항 업데이트 시 첨부파일과 함께 저장")
    void saveOrUpdateNotice_ShouldUpdateExistingNoticeWithAttachments() {
        // given
        // 테스트용 첨부 파일 생성
        MockMultipartFile file1 = new MockMultipartFile("file", "test.txt", "text/plain", "Hello, World!".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file", "test2.txt", "text/plain", "Hello again!".getBytes());

        // 첨부 파일 목록 설정
        noticeDto.setAttachments(List.of(file1, file2));

        // NoticeDto에서 Notice 엔티티로 변환하는 Mock 설정
        given(noticeMapper.toEntity(noticeDto)).willReturn(notice);

        // NoticeRepository의 save 메서드 Mock 설정
        given(noticeRepository.save(any(Notice.class))).willReturn(notice);

        // 파일 업로드 경로 설정
        String uploadDir = "C:/fileStorage/notice/";

        // NoticeService의 saveOrUpdateNotice 메서드 호출
        Notice result = noticeService.saveOrUpdateNotice(noticeDto);

        // 테스트 결과 검증
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Test Title");
        assertThat(result.getContent()).isEqualTo("Test Content");
        assertThat(result.getAttachments()).hasSize(2); // 첨부 파일 수 검증

        // 첨부 파일 저장 경로에 파일이 잘 저장되었는지 검증
        Path filePath1 = Paths.get(uploadDir + file1.getOriginalFilename());
        Path filePath2 = Paths.get(uploadDir + file2.getOriginalFilename());
        assertThat(Files.exists(filePath1)).isTrue();
        assertThat(Files.exists(filePath2)).isTrue();

        // Mockito verify를 통해 메서드 호출 횟수 검증
        then(noticeMapper).should().toEntity(noticeDto);
        then(noticeRepository).should().save(notice);
    }


}