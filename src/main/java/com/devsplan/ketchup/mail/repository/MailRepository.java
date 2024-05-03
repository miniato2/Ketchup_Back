package com.devsplan.ketchup.mail.repository;

import com.devsplan.ketchup.mail.entity.Mail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MailRepository extends JpaRepository<Mail, Integer> {
    List<Mail> findBySenderMemAndSendDelStatusAndMailTitleContaining(String senderMem, char delStatus, String searchValue);
//    List<Mail> findBySenderMemAndSendDelStatusAndReceiMemContaining(String senderMem, char delStatus, String searchValue);
    Mail findByMailNoAndSendDelStatus(int mailNo, char delStatus);
    Mail findByMailNo(int mailNo);
    List<Mail> findByMailTitleContaining(String searchValue);
    List<Mail> findBySenderMemContaining(String searchValue);


    // 백업
    List<Mail> findBySenderMemAndSendDelStatus(String senderMem, char delStatus);
//    Mail findByMailNoAndSendDelStatus(int mailNo, char delStatus);
//    Mail findByMailNo(int mailNo);
//    List<Mail> findByMailTitleLike(String searchValue);
//    List<Mail> findBySenderMemLike(String searchValue);
}