package com.devsplan.ketchup.mail.repository;

import com.devsplan.ketchup.mail.entity.MailFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailFileRepository extends JpaRepository<MailFile, Integer> {

}
