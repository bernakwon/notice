package com.berna.notice.repository;


import com.berna.notice.model.NoticeAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeAttachmentRepository extends JpaRepository<NoticeAttachment, Long> {
    List<NoticeAttachment> findByNoticeId(Long id);
}
