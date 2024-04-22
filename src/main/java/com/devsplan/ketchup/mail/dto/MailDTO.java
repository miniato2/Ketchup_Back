package com.devsplan.ketchup.mail.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MailDTO {
    private int mailNo;                 // 메일 번호
    private String sender;              // 발신자
    private String mailTitle;           // 메일 제목
    private String mailContent;         // 메일 내용
    private char sendCancelStatus;      // 메일 발송 취소 여부
    private char sendDelStatus;         // 발신 메일 삭제 여부
}
