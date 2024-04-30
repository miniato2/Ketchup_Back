package com.devsplan.ketchup.notice.repository;

import com.devsplan.ketchup.notice.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository <Notice, Integer> {

    Page<Notice> findByNoticeFix(Pageable pageable, char y);

    Page<Notice> findByNoticeTitleContainingAndNoticeFix(Pageable pageable, String title, char n);

    Page<Notice> findByNoticeTitleContaining(Pageable pageable, String title);
}
