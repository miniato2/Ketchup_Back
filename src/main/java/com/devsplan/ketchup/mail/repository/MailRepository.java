package com.devsplan.ketchup.mail.repository;

import com.devsplan.ketchup.mail.entity.Mail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MailRepository extends JpaRepository<Mail, Integer> {
    List<Mail> findBySenderMemAndSendDelStatus(String senderMem, char delStatus);
    Mail findByMailNoAndSendDelStatus(int mailNo, char delStatus);
    Mail findByMailNo(int mailNo);
}