package com.devsplan.ketchup.mail.dto;

import lombok.*;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ReceiverDTO {
    private int receiverNo;             // 수신자 번호
    private int mailNo;                 // 메일 번호
    private String receiverName;        // 수신자 이름
    private Timestamp readTime;         // 메일 읽은 시간
    private char receiverDelStatus;     // 수신 메일 삭제 여부
}

