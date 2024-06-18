package com.berna.notice.repository;


import com.berna.notice.model.NoticeAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeAttachmentRepository extends JpaRepository<NoticeAttachment, Long> {
}
