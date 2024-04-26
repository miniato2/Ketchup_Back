package com.devsplan.ketchup.mail.repository;

import com.devsplan.ketchup.mail.dto.ReceiverDTO;
import com.devsplan.ketchup.mail.entity.Receiver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceiverRepository  extends JpaRepository<Receiver, Integer> {
    List<Receiver> findByReceiverMem(int receiverTest);
}
