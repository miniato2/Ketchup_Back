package com.devsplan.ketchup.mail.service;

import com.devsplan.ketchup.mail.dto.MailDTO;
import com.devsplan.ketchup.mail.dto.ReceiverDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class MailRestTests {
    @Autowired
    private MailService mailService;

    @DisplayName("메일 전송 - ㅇ메일 내용, ㅇ수신자(한명, 여러명), 첨부 파일")
    @Test
    void insertMail() throws IOException {
        // given
        MailDTO newMail = new MailDTO(
                240429002,
                "3[자료 요청] 1분기 실적보고서 내 영업실 데이터",
                "3지난번 공유주신 1분기 실적보고서는 잘 받았습니다. 리포터에 산입하신 영업팀의 지난분기 계약 및 실적 데이터 전달을 부탁드리겠습니다.",
                'N',
                'N'
        );

        int mailNo = mailService.insertMail(newMail);

        ReceiverDTO receiverInfo1 = new ReceiverDTO(
                mailNo,
                240429001,
                null,
                'N'
        );

        ReceiverDTO receiverInfo2 = new ReceiverDTO(
                mailNo,
                240429003,
                null,
                'N'
        );

        List<ReceiverDTO> receivers = new ArrayList<>();
        receivers.add(receiverInfo1);
        receivers.add(receiverInfo2);

        // then
        Assertions.assertDoesNotThrow(() -> mailService.insertReceiver(receivers));
    }

    @DisplayName("보낸 메일 목록 조회 - ㅇ목록, ㅇ삭제 되지 않은 목록, 검색, 페이징, 읽음 여부 확인, 수신자")
    @Test
    void selectSendMailList() {
        // given
        int senderTest = 240429002;

        // when
        List<MailDTO> mailList = mailService.selectSendMailList(senderTest);

        // then
        Assertions.assertNotNull(mailList);
        for(MailDTO mail : mailList) {
            System.out.println(senderTest + "보낸 메일 목록 : " + mail);
        }
    }

    @DisplayName("받은 메일 목록 조회 - ㅇ목록, ㅇ삭제 되지 않은 목록, 검색, 페이징")
    @Test
    void selectReceiveMailList() {
        // given
        int receiverTest = 240429003;

        // when
        List<MailDTO> mailList = mailService.selectReceiveMailList(receiverTest);

        // then
        Assertions.assertNotNull(mailList);
        for(MailDTO mail : mailList) {
            System.out.println(receiverTest + "받은 메일 목록 : " + mail);
        }
    }

    @DisplayName("메일 상세 조회 - ㅇ메일 내용, 수신자, 첨부 파일")
    @Test
    void selectMailDetail() {
        // given
        int mailNo = 3;

        // when
        MailDTO mailDetail = mailService.selectMailDetail(mailNo);

        // then
        Assertions.assertNotNull(mailDetail);
        System.out.println(mailNo + "번 메일 : " + mailDetail);
    }

    @DisplayName("발송 취소 - ㅇ메일 발송 여부 변경, ㅇ수신자 삭제 여부 변경")
    @Test
    void cancelSendMail() {
        // given
        int mailNo = 2;

        // when
        String result = mailService.cancelSendMail(mailNo);

        // then
        Assertions.assertEquals("발송 취소 성공", result);
    }

    @DisplayName("보낸 메일 삭제 - ㅇ")
    @Test
    void deleteSendMail() {
        // given
        int mailNo = 1;

        // when
        int result = mailService.deleteSendMail(mailNo);

        // then
        Assertions.assertNotEquals(0 , result);
    }

    @DisplayName("받은 메일 삭제 - ㅇ")
    @Test
    void deleteReceiveMail() {
        // given
        int mailNo = 3;

        // when
        int result = mailService.deleteReceiveMail(mailNo);

        // then
        Assertions.assertNotEquals(0, result);
    }

    @DisplayName("답장")
    @Test
    void replyMail() {
        // given
        // when
        // then
    }
}
