package com.devsplan.ketchup.mail.repository;

import com.devsplan.ketchup.mail.entity.Receiver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceiverRepository  extends JpaRepository<Receiver, Integer> {

    List<Receiver> findByReceiverMem(int receiverTest);

    @Query("SELECT r FROM Receiver r WHERE r.mailNo = :mailNo")
    List<Receiver> findReadTime(int mailNo);

    @Modifying
    @Query("UPDATE Receiver r SET r.receiverDelStatus = 'Y' WHERE r.mailNo = :mailNo and r.receiverMem = :receiverMem")
    int updateByReceiverDelStatus(int mailNo, int receiverMem);
}
