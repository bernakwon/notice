package com.berna.notice;

import com.berna.notice.dto.NoticeDto;
import com.berna.notice.dto.NoticeResponseDto;
import com.berna.notice.model.Notice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class NoticeIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String BASE_URL = "/api/notices";

    private NoticeDto noticeDto;
    private Notice notice;


    @BeforeEach
    void setUp() throws URISyntaxException, IOException {
        // NoticeDto 설정
        noticeDto = new NoticeDto();
        noticeDto.setTitle("Test Notice Title");
        noticeDto.setContent("Test Notice Content");


        // 미리 공지사항 생성
         createInitialNotice();
    }

    private void createInitialNotice() throws URISyntaxException, IOException {
        URI uri = new URI(createURLWithPort(BASE_URL));
        // NoticeDto 생성
        NoticeDto newNoticeDto = new NoticeDto();
        newNoticeDto.setTitle("Notice Title");
        newNoticeDto.setContent("Notice Content");


        // 첨부 파일 생성
        MockMultipartFile file1 = new MockMultipartFile("file", "test.txt", "text/plain", "Hello, World!".getBytes(StandardCharsets.UTF_8));
        MockMultipartFile file2 = new MockMultipartFile("file", "test2.txt", "text/plain", "Hello again!".getBytes(StandardCharsets.UTF_8));

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("noticeDto", new HttpEntity<>(newNoticeDto, createJsonHeaders()));
        body.add("files", getResource(file1));
        body.add("files", getResource(file2));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Notice> responseEntity = restTemplate.postForEntity(uri, requestEntity, Notice.class);
        notice = responseEntity.getBody();
    }

    private ByteArrayResource getResource(MockMultipartFile file) throws IOException {
        return new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };
    }
    private HttpHeaders createJsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    @Test
    @DisplayName("공지사항 생성")
    void createNotice_ShouldCreateNotice() throws URISyntaxException, IOException {
        URI uri = new URI(createURLWithPort(BASE_URL));

        // NoticeDto 생성
        NoticeDto newNoticeDto = new NoticeDto();
        newNoticeDto.setTitle("New Notice Title");
        newNoticeDto.setContent("New Notice Content");


        // 첨부 파일 생성
        MockMultipartFile file1 = new MockMultipartFile("file", "test.txt", "text/plain", "Hello, World!".getBytes(StandardCharsets.UTF_8));
        MockMultipartFile file2 = new MockMultipartFile("file", "test2.txt", "text/plain", "Hello again!".getBytes(StandardCharsets.UTF_8));

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("noticeDto", new HttpEntity<>(newNoticeDto, createJsonHeaders()));
        body.add("files", getResource(file1));
        body.add("files", getResource(file2));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Notice> responseEntity = restTemplate.postForEntity(uri, requestEntity, Notice.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();


        // 테스트 후 첨부 파일 삭제
        cleanupAttachments(file1, file2);
    }

    @Test
    @DisplayName("특정 공지사항 조회")
    void getNoticeById_ShouldReturnNotice() throws URISyntaxException {
        Long id = notice.getId();
        NoticeResponseDto response = restTemplate.getForObject(createURLWithPort(BASE_URL + "/" + id), NoticeResponseDto.class);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getTitle()).isEqualTo(notice.getTitle());
    }

    @Test
    @DisplayName("공지사항 수정")
    void updateNotice_ShouldUpdateNotice() throws URISyntaxException, IOException {
        Long id = 1L;
        noticeDto.setId(id);
        noticeDto.setTitle("Updated Title");

        // 새로운 첨부 파일 생성
        MockMultipartFile file3 = new MockMultipartFile("file", "test3.txt", "text/plain", "Updated content!".getBytes(StandardCharsets.UTF_8));

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("noticeDto", new HttpEntity<>(noticeDto, createJsonHeaders()));
        body.add("files", new ByteArrayResource(file3.getBytes()) {
            @Override
            public String getFilename() {
                return file3.getOriginalFilename();
            }
        });

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        URI uri = new URI(createURLWithPort(BASE_URL + "/" + id));
        ResponseEntity<Notice> responseEntity = restTemplate.exchange(uri, HttpMethod.PUT, requestEntity, Notice.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();

        // 테스트 후 첨부 파일 삭제
        cleanupAttachments(file3);
    }

    @Test
    @DisplayName("공지사항 삭제")
    void deleteNoticeById_ShouldDeleteNotice() throws URISyntaxException {
        Long id = 1L;
        URI uri = new URI(createURLWithPort(BASE_URL + "/" + id));

        ResponseEntity<Void> responseEntity = restTemplate.exchange(uri, HttpMethod.DELETE, null, Void.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    private void cleanupAttachments(MockMultipartFile... files) {
        for (MockMultipartFile file : files) {
            try {
                file.getInputStream().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}