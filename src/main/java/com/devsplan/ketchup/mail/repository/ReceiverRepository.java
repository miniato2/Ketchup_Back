package com.devsplan.ketchup.mail.repository;

import com.devsplan.ketchup.mail.entity.Receiver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceiverRepository  extends JpaRepository<Receiver, Integer> {
    List<Receiver> findByReceiverMem(int receiverTest);

    @Query("SELECT r FROM Receiver r WHERE r.mailNo = :mailNo")
    List<Receiver> findReadTime(int mailNo);

    @Modifying
    @Query("UPDATE Receiver r SET r.receiverDelStatus = 'Y' WHERE r.mailNo = :mailNo and r.receiverMem = :receiverMem")
    int updateDelByMailNoReceiver(int mailNo, int receiverMem);

    @Modifying
    @Query("UPDATE Receiver r SET r.receiverDelStatus = 'Y' WHERE r.mailNo = :mailNo")
    void updateReceiverDelByMailNo(int mailNo);
}