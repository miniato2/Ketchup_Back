package com.devsplan.ketchup.mail.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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

    @Column(name = "SENDER", length = 50, nullable = false)
    private String sender;

    @Column(name = "MAIL_TITLE", length = 200, nullable = false)
    private String mailTitle;

    @Column(name = "MAIL_CONTENT", length = 3000, nullable = false)
    private String mailContent;

    @Column(name = "SEND_CANCEL_STATUS", nullable = false)
    private char sendCancelStatus;

    @Column(name = "SEND_DEL_STATUS", nullable = false)
    private char sendDelStatus;

//    @OneToMany(mappedBy = "mailNo")
//    private List<MailFile> mailFiles;

//    @Setter
//    @OneToMany(mappedBy = "mailNo", cascade = CascadeType.PERSIST)
//    private List<Receiver> Receivers;

    protected Mail() {}

    public Mail(int mailNo, String sender, String mailTitle, String mailContent, char sendCancelStatus, char sendDelStatus) {
        this.mailNo = mailNo;
        this.sender = sender;
        this.mailTitle = mailTitle;
        this.mailContent = mailContent;
        this.sendCancelStatus = sendCancelStatus;
        this.sendDelStatus = sendDelStatus;
    }
}
