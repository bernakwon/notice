package com.berna.notice;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;

    private final NoticeMapper noticeMapper;

    @Transactional(readOnly = true)
    public List<NoticeResponseDto> getAllNotices() {
        return noticeRepository.findAll().stream()
                .map(noticeMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public NoticeResponseDto getNoticeById(Long id) {

        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Notice not found with id: " + id));

        notice.increaseViewCount();
        noticeRepository.save(notice); // 조회수 증가 후 저장
        return noticeMapper.toResponseDto(notice);
    }

    @Transactional
    public Notice saveOrUpdateNotice(NoticeDto noticeDto) {
        Notice notice = noticeMapper.toEntity(noticeDto);
        if(notice.getId() != null) {
            Notice originalNotice = noticeRepository.findById(notice.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Notice not found with id: " + notice.getId()));
            notice.setAttachments(originalNotice.getAttachments());
        }

        return noticeRepository.save(notice);
    }

}
