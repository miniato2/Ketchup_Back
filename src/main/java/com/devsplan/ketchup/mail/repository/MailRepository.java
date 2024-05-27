package com.devsplan.ketchup.mail.repository;

import com.devsplan.ketchup.mail.entity.Mail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MailRepository extends JpaRepository<Mail, Integer> {
    Page<Mail> findBySenderMemAndSendDelStatusAndMailTitleContaining(String senderMem, char delStatus, String searchValue, Pageable paging);
//    Mail findByMailNoAndSendDelStatus(int mailNo, char delStatus);
    Mail findByMailNo(int mailNo);
    List<Mail> findByMailTitleContaining(String searchValue);

//    Page<Mail> findBySenderMemContaining(String searchValue, Pageable paging); // 발신자, 수신자 검색
    Page<Mail> findBySenderMemAndSendDelStatus(String senderMem, char delStatus, Pageable paging);
}