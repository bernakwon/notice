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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 조회됨",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = NoticeResponseDto.class))}),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content)})
    @GetMapping
    public PageDto<NoticeResponseDto> getAllNotices(@RequestParam int page, @RequestParam int size) {
        Page<Notice> noticePage = noticeService.getAllNotices(PageRequest.of(page, size));
        List<NoticeResponseDto> content = noticePage.getContent().stream()
                .map(notice -> new NoticeResponseDto(notice.getId(), notice.getTitle(), notice.getContent(),
                        notice.getCreatedDate(), notice.getViewCount(), notice.getAuthor()))
                .collect(Collectors.toList());
        return new PageDto<>(noticePage.getNumber(), noticePage.getTotalPages(), noticePage.getTotalElements(), noticePage.getSize(), content);
    }

    @Operation(summary = "특정 공지사항 조회", description = "ID로 특정 공지사항을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 조회됨",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = NoticeResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "공지사항을 찾을 수 없음",
                    content = @Content)})
    @GetMapping("/{id}")
    public NoticeResponseDto getNoticeById(@PathVariable Long id) {
        return noticeService.getNoticeById(id);
    }

    @Operation(summary = "공지사항 생성", description = "새로운 공지사항을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공적으로 생성됨",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Notice.class))}),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content)})
    @PostMapping
    public Notice createNotice(@RequestBody NoticeDto noticeDto) {
        return noticeService.saveOrUpdateNotice(noticeDto);
    }

    @Operation(summary = "공지사항 업데이트", description = "ID로 기존 공지사항을 업데이트합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 업데이트됨",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Notice.class))}),
            @ApiResponse(responseCode = "404", description = "공지사항을 찾을 수 없음",
                    content = @Content)})
    @PutMapping("/{id}")
    public Notice updateNotice(@PathVariable Long id, @RequestPart("noticeDto") NoticeDto noticeDto,  @RequestPart("files") List<MultipartFile> files) {
        noticeDto.setId(id);
        return noticeService.saveOrUpdateNotice(noticeDto);
    }

    @Operation(summary = "공지사항 삭제", description = "ID로 기존 공지사항을 삭제합니다.")
    @DeleteMapping("/{id}")
    public void deleteNotice(@PathVariable Long id) {
        noticeService.deleteNoticeById(id);
    }

}