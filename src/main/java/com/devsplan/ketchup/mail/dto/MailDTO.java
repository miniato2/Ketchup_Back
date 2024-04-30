package com.devsplan.ketchup.mail.dto;

import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@ToString
public class MailDTO {
    private int mailNo;                     // 메일 번호
    private int senderMem;                  // 발신자 사원 번호
    private String mailTitle;               // 메일 제목
    private String mailContent;             // 메일 내용
    private Timestamp sendMailTime;         // 보낸 메일 시간
    private char sendCancelStatus;          // 메일 발송 취소 여부
    private char sendDelStatus;             // 발신 메일 삭제 여부
    private List<ReceiverDTO> receivers;    // 수신자 목록
    private List<MailFileDTO> mailFiles;    // 첨부 파일 목록

    public MailDTO() {
    }

    public MailDTO(int senderMem, String mailTitle, String mailContent, char sendCancelStatus, char sendDelStatus) {
        this.senderMem = senderMem;
        this.mailTitle = mailTitle;
        this.mailContent = mailContent;
        this.sendCancelStatus = sendCancelStatus;
        this.sendDelStatus = sendDelStatus;
    }

    public MailDTO(int mailNo, int senderMem, String mailTitle, String mailContent, Timestamp sendMailTime, char sendCancelStatus, char sendDelStatus) {
        this.mailNo = mailNo;
        this.senderMem = senderMem;
        this.mailTitle = mailTitle;
        this.mailContent = mailContent;
        this.sendMailTime = sendMailTime;
        this.sendCancelStatus = sendCancelStatus;
        this.sendDelStatus = sendDelStatus;
    }
}