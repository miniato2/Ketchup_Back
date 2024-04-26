package com.devsplan.ketchup.mail.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
public class MailDTO {
    private int mailNo;                     // 메일 번호
    private int senderMem;               // 발신자 사원 번호
    private String mailTitle;               // 메일 제목
    private String mailContent;             // 메일 내용
    private char sendCancelStatus;          // 메일 발송 취소 여부
    private char sendDelStatus;             // 발신 메일 삭제 여부
    private List<ReceiverDTO> receivers;

    public MailDTO() {
    }

    public MailDTO(int mailNo, int senderMem, String mailTitle, String mailContent, char sendCancelStatus, char sendDelStatus) {
        this.mailNo = mailNo;
        this.senderMem = senderMem;
        this.mailTitle = mailTitle;
        this.mailContent = mailContent;
        this.sendCancelStatus = sendCancelStatus;
        this.sendDelStatus = sendDelStatus;
    }
}
