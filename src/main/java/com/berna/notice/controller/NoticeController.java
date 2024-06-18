package com.berna.notice.controller;

import com.berna.notice.dto.PageDto;
import com.berna.notice.service.NoticeService;
import com.berna.notice.dto.NoticeDto;
import com.berna.notice.dto.NoticeResponseDto;
import com.berna.notice.model.Notice;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notices")
@Tag( name = "Notice API" , description = "공지사항 관련 API")
public class NoticeController {
    @Autowired
    private NoticeService noticeService;
    @Operation(summary = "모든 공지사항 조회", description = "모든 공지사항을 조회합니다.")
    @GetMapping
    public PageDto<NoticeResponseDto> getAllNotices(@RequestParam int page, @RequestParam int size) {
        Page<Notice> noticePage = noticeService.getAllNotices(PageRequest.of(page, size));
        List<NoticeResponseDto> content = noticePage.getContent().stream()
                .map(notice -> new NoticeResponseDto(notice.getId(), notice.getTitle(), notice.getContent(),
                        notice.getCreatedDate(), notice.getViewCount(), notice.getAuthor() ))
                .collect(Collectors.toList());
        return new PageDto<>(noticePage.getNumber(), noticePage.getTotalPages(), noticePage.getTotalElements(), noticePage.getSize(), content);
    }

    @Operation(summary = "특정 공지사항 조회", description = "ID로 특정 공지사항을 조회합니다.")
    @GetMapping("/{id}")
    public NoticeResponseDto getNoticeById(@PathVariable Long id) {
        return noticeService.getNoticeById(id);
    }

    @Operation(summary = "공지사항 생성", description = "새로운 공지사항을 생성합니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Notice createNotice(@RequestPart("noticeDto") NoticeDto noticeDto,  @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        noticeDto.setAttachments(files);
        return noticeService.saveOrUpdateNotice(noticeDto);
    }

    @Operation(summary = "공지사항 업데이트", description = "ID로 기존 공지사항을 업데이트합니다.")
    @PutMapping(value = "/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Notice updateNotice(@PathVariable Long id, @RequestPart("noticeDto") NoticeDto noticeDto,  @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        noticeDto.setId(id);
        noticeDto.setAttachments(files);
        return noticeService.saveOrUpdateNotice(noticeDto);
    }

    @Operation(summary = "공지사항 삭제", description = "ID로 기존 공지사항을 삭제합니다.")
    @DeleteMapping("/{id}")
    public void deleteNotice(@PathVariable Long id) {
        noticeService.deleteNoticeById(id);
    }

}