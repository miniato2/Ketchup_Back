package com.devsplan.ketchup.mail.repository;

import com.devsplan.ketchup.mail.entity.Mail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MailRepository extends JpaRepository<Mail, Integer> {
    List<Mail> findBySenderMemAndSendDelStatusAndMailTitleContaining(String senderMem, char delStatus, String searchValue);
    Mail findByMailNoAndSendDelStatus(int mailNo, char delStatus);
    Mail findByMailNo(int mailNo);
    List<Mail> findByMailTitleContaining(String searchValue);
    List<Mail> findBySenderMemContaining(String searchValue);
    List<Mail> findBySenderMemAndSendDelStatus(String senderMem, char delStatus);
}