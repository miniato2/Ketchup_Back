package com.devsplan.ketchup.notice.repository;

import com.devsplan.ketchup.notice.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository <Notice, Integer> {

    Page<Notice> findByNoticeFix(Pageable pageable, char y);
    Page<Notice> findByNoticeTitleLikeIgnoreCase(String formattedTitle, Pageable pageable);

    Page<Notice> findByNoticeTitleContaining(String formattedTitle, Pageable pageable);
}
