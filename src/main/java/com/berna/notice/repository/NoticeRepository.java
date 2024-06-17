package com.berna.notice.repository;

import com.berna.notice.model.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository  extends JpaRepository<Notice, Long> {
    Page<Notice> findAll(Pageable pageable);
}
