package com.devsplan.ketchup.notice.repository;

import com.devsplan.ketchup.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository <Notice, Integer> {
    //
}
