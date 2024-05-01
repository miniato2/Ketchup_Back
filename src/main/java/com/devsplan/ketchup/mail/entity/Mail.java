package com.devsplan.ketchup.mail.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "tbl_mail")
@Getter
@ToString
public class Mail {
    @Id
    @Column(name = "MAIL_NO", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mailNo;

    @Column(name = "SENDER_MEM", length = 50, nullable = false)
    private String senderMem;

    @Column(name = "MAIL_TITLE", length = 200, nullable = false)
    private String mailTitle;

    @Column(name = "MAIL_CONTENT", length = 3000, nullable = false)
    private String mailContent;

    @CreationTimestamp
    @Column(name = "SEND_MAIL_TIME", nullable = false)
    private Timestamp sendMailTime;

    @Setter
    @Column(name = "SEND_CANCEL_STATUS", nullable = false)
    private char sendCancelStatus;

    @Setter
    @Column(name = "SEND_DEL_STATUS", nullable = false)
    private char sendDelStatus;

    @Setter
    @OneToMany(mappedBy = "mailNo", cascade = CascadeType.PERSIST)
    private List<Receiver> Receivers;

//    @OneToMany(mappedBy = "mailNo")
//    private List<MailFile> mailFiles;

//    public Mail mailNo(int val) {
//        this.mailNo = val;
//        return this;
//    }
//
//    public Mail senderMem(String val) {
//        this.senderMem = val;
//        return this;
//    }
//
//    public Mail mailTitle(String val) {
//        this.mailTitle  = val;
//        return this;
//    }
//    public Mail mailContent(String val) {
//        this.mailContent = val;
//        return this;
//    }
//    public Mail sendMailTime(Timestamp val) {
//        this.sendMailTime  = val;
//        return this;
//    }
//    public Mail sendCancelStatus(char val) {
//        this.sendCancelStatus  = val;
//        return this;
//    }
//    public Mail sendDelStatus(char val) {
//        this.sendDelStatus  = val;
//        return this;
//    }
//
//    public Mail builder() {
//        return new Mail(mailNo, senderMem, mailTitle, mailContent, sendMailTime, sendCancelStatus, sendDelStatus);
//    }

    protected Mail() {}

    public Mail(String senderMem, String mailTitle, String mailContent, char sendCancelStatus, char sendDelStatus) {
        this.senderMem = senderMem;
        this.mailTitle = mailTitle;
        this.mailContent = mailContent;
        this.sendCancelStatus = sendCancelStatus;
        this.sendDelStatus = sendDelStatus;
    }

    public Mail(int mailNo, String senderMem, String mailTitle, String mailContent, Timestamp sendMailTime, char sendCancelStatus, char sendDelStatus) {
        this.mailNo = mailNo;
        this.senderMem = senderMem;
        this.mailTitle = mailTitle;
        this.mailContent = mailContent;
        this.sendMailTime = sendMailTime;
        this.sendCancelStatus = sendCancelStatus;
        this.sendDelStatus = sendDelStatus;
    }

    public Mail(int mailNo, String senderMem, String mailTitle, String mailContent, Timestamp sendMailTime, char sendCancelStatus, char sendDelStatus, List<Receiver> receivers) {
        this.mailNo = mailNo;
        this.senderMem = senderMem;
        this.mailTitle = mailTitle;
        this.mailContent = mailContent;
        this.sendMailTime = sendMailTime;
        this.sendCancelStatus = sendCancelStatus;
        this.sendDelStatus = sendDelStatus;
        Receivers = receivers;
    }
}