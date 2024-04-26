package com.devsplan.ketchup.mail.repository;

import com.devsplan.ketchup.mail.entity.Mail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MailRepository extends JpaRepository<Mail, Integer> {

    List<Mail> findBySenderMem(int senderMem);

    Mail findByMailNo(int i);

    @Modifying
    @Query("UPDATE Mail m SET m.sendCancelStatus = 'Y' WHERE m.mailNo = :mailNo")
    int cancelSendMail(int mailNo);

    @Modifying
    @Query("UPDATE Mail m SET m.sendDelStatus = 'Y' WHERE m.mailNo = :mailNo and m.senderMem = :senderMem")
    int updateBySendDelStatus(int mailNo, int senderMem);

}
