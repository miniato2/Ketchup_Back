package com.devsplan.ketchup.mail.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "tbl_mail_file")
@Getter
@Setter
@ToString
public class MailFile {
    @Id
    @Column(name = "MAIL_FILE_NO", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mailFileNo;

    @Column(name = "MAIL_NO", nullable = false)
    private int mailNo;

    @Column(name = "MAIL_FILE_PATH", nullable = false)
    private String mailFilePath;

    @Column(name = "MAIL_FILE_NAME", nullable = false)
    private String mailFileName;

    @Column(name = "MAIL_FILE_ORI_NAME", nullable = false)
    private String mailFileOriName;

    public MailFile() {}

    public MailFile(int mailFileNo, int mailNo, String mailFilePath, String mailFileName, String mailFileOriName) {
        this.mailFileNo = mailFileNo;
        this.mailNo = mailNo;
        this.mailFilePath = mailFilePath;
        this.mailFileName = mailFileName;
        this.mailFileOriName = mailFileOriName;
    }
}
