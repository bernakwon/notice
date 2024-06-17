package com.berna.notice;

import com.berna.notice.dto.NoticeDto;
import com.berna.notice.dto.NoticeResponseDto;
import com.berna.notice.dto.PageDto;
import com.berna.notice.model.Notice;
import com.berna.notice.repository.NoticeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NoticeIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Notice notice;
    private NoticeDto noticeDto;

    @BeforeEach
    void setUp() {
        noticeRepository.deleteAll();

        notice = new Notice();
        notice.setTitle("Test Title");
        notice.setContent("Test Content");
        notice.setAuthor("Test Author");
        noticeRepository.save(notice);

        noticeDto = new NoticeDto();
        noticeDto.setTitle("New Test Title");
        noticeDto.setContent("New Test Content");
        noticeDto.setAuthor("Test Author");
    }

    @Test
    @DisplayName("공지사항 목록 조회")
    void getAllNotices_ShouldReturnPagedNotices(){
        URI uri = UriComponentsBuilder.fromHttpUrl(createURLWithPort("/api/notices"))
                .queryParam("page", 0)
                .queryParam("size", 10)
                .build().toUri();

        ResponseEntity<PageDto<NoticeResponseDto>> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageDto<NoticeResponseDto>>() {});

        assertTrue("Response should be 2xx", response.getStatusCode().is2xxSuccessful());
        assertThat(response.getBody()).isNotNull();

    }


    @Test
    @DisplayName("특정 공지사항 조회")
    void getNoticeById_ShouldReturnNotice() {
        Long id = notice.getId();
        NoticeResponseDto response = restTemplate.getForObject(createURLWithPort("/api/notices/" + id), NoticeResponseDto.class);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getTitle()).isEqualTo(notice.getTitle());
    }

    @Test
    @DisplayName("공지사항 생성")
    void createNotice_ShouldCreateNotice() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<NoticeDto> request = new HttpEntity<>(noticeDto, headers);
        Notice response = restTemplate.postForObject(createURLWithPort("/api/notices"), request, Notice.class);

        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo(noticeDto.getTitle());
    }

    @Test
    @DisplayName("공지사항 수정")
    void updateNotice_ShouldUpdateNotice() {
        Long id = notice.getId();
        noticeDto.setTitle("Updated Title");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<NoticeDto> request = new HttpEntity<>(noticeDto, headers);
        ResponseEntity<Notice> response = restTemplate.exchange(createURLWithPort("/api/notices/" + id), HttpMethod.PUT, request, Notice.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Updated Title");
    }

    @Test
    @DisplayName("공지사항 삭제")
    void deleteNotice_ShouldDeleteNotice() {
        Long id = notice.getId();

        restTemplate.delete(createURLWithPort("/api/notices/" + id));

        assertThat(noticeRepository.findById(id)).isEmpty();
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}