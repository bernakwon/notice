package com.berna.notice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notices")
public class NoticeController {
    @Autowired
    private NoticeService noticeService;

    @GetMapping
    public List<NoticeResponseDto> getAllNotices() {
        return noticeService.getAllNotices();
    }

    @GetMapping("/{id}")
    public NoticeResponseDto getNoticeById(@PathVariable Long id) {
        return noticeService.getNoticeById(id);
    }


}