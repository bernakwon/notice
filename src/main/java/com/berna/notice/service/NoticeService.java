package com.berna.notice.service;

import com.berna.notice.service.mapper.NoticeMapper;
import com.berna.notice.repository.NoticeRepository;
import com.berna.notice.dto.NoticeDto;
import com.berna.notice.dto.NoticeResponseDto;
import com.berna.notice.model.Notice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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

        notice.setViewCount(notice.getViewCount() + 1);
        noticeRepository.save(notice); // 조회수 증가 후 저장
        return noticeMapper.toResponseDto(notice);
    }

    @Transactional
    public Notice saveOrUpdateNotice(NoticeDto noticeDto) {

        Optional<Notice> targetNotice = noticeRepository.findById(noticeDto.getId());
        if(targetNotice.isPresent()){
            Long targetNoticeId = targetNotice.get().getId();
            noticeDto.setId(targetNoticeId);
        }

        Notice result = noticeMapper.toEntity(noticeDto);;

        return noticeRepository.save(result);
    }


    @Transactional
    public void deleteNotice(Long id) {
        noticeRepository.deleteById(id);

    }


}
