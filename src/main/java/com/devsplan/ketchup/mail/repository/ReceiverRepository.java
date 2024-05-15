package com.devsplan.ketchup.mail.repository;

import com.devsplan.ketchup.mail.entity.Receiver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ReceiverRepository  extends JpaRepository<Receiver, Integer> {
    List<Receiver> findByReceiverMemAndReceiverDelStatus(String receiverMem, char delStatus);
    List<Receiver> findByMailNo(int mailNo);
    Receiver findByMailNoAndReceiverMem(int mailNo, String memberNo);
}