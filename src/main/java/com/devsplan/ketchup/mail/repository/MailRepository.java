package com.devsplan.ketchup.mail.repository;

import com.devsplan.ketchup.mail.dto.MailDTO;
import com.devsplan.ketchup.mail.entity.Mail;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MailRepository extends JpaRepository<Mail, Integer> {
    List<Mail> findBySender(String senderNo);
}
