package com.devsplan.ketchup.mail.repository;

import com.devsplan.ketchup.mail.entity.Mail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MailRepository extends JpaRepository<Mail, Integer> {
    Page<Mail> findBySenderMemAndSendDelStatusAndMailTitleContaining(String senderMem, char delStatus, String searchValue, Pageable paging);
    Mail findByMailNoAndSendDelStatus(int mailNo, char delStatus);
    Mail findByMailNo(int mailNo);
    Page<Mail> findByMailTitleContaining(String searchValue, Pageable paging);
//    Page<Mail> findBySenderMemContaining(String searchValue, Pageable paging);
    Page<Mail> findBySenderMemAndSendDelStatus(String senderMem, char delStatus, Pageable paging);
}