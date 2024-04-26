package com.devsplan.ketchup.mail;

import com.devsplan.ketchup.mail.dto.MailDTO;
import com.devsplan.ketchup.mail.dto.ReceiverDTO;
import com.devsplan.ketchup.mail.service.MailService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Stream;

@SpringBootTest
public class MailRestTests {
    @Autowired
    private MailService mailService;

    private static Stream<Arguments> getMail() {
        return Stream.of(
                Arguments.of(
                        3,
                        240425003,
                        "3[자료 요청] 1분기 실적보고서 내 영업실 데이터",
                        "3지난번 공유주신 1분기 실적보고서는 잘 받았습니다. 리포터에 산입하신 영업팀의 지난분기 계약 및 실적 데이터 전달을 부탁드리겠습니다.",
                        'N',
                        'N',
                        3,
                        240425001,
                        null,
                        'N'
                )
        );
    }

    @DisplayName("메일 전송")
    @ParameterizedTest
    @MethodSource("getMail")
    void insertMail(int mailNo, int senderMem, String mailTitle, String mailContent, char sendCancelStatus, char sendDelStatus,
                    int receiverNo, int receiverMem, Timestamp readTime, char receiverDelStatus) {
        // given
        MailDTO newMail = new MailDTO(
                mailNo, senderMem, mailTitle, mailContent, sendCancelStatus, sendDelStatus
        );

        ReceiverDTO receiverInfo = new ReceiverDTO(
                receiverNo,
                mailNo,
                receiverMem,
                readTime,
                receiverDelStatus
        );

//        List<ReceiverDTO>receiverList = new ArrayList<>();
//        receiverList.add(receiverInfo);

        // when

        // then
        Assertions.assertDoesNotThrow(() -> mailService.insertMail(newMail, receiverInfo));
        System.out.println("메일 정보" + newMail);
        System.out.println("수신자 정보" + receiverInfo);
    }

    @DisplayName("보낸 메일 목록 조회")
    @Test
    void selectSendMailList() {
        // given
        int senderTest = 240425003;

        // when
        List<MailDTO> mailList = mailService.selectSendMailList(senderTest);

        // then
        Assertions.assertNotNull(mailList);
        for(MailDTO mail : mailList) {
            System.out.println(senderTest + "보낸 메일 목록 : " + mail);
        }
    }

    @DisplayName("받은 메일 목록 조회")
    @Test
    void selectReceiveMailList() {
        // given
        int receiverTest = 240425002;

        // when
        List<MailDTO> mailList = mailService.selectReceiveMailList(receiverTest);

        // then
        Assertions.assertNotNull(mailList);
        for(MailDTO mail : mailList) {
            System.out.println(receiverTest +  "받은 메일 목록 : " + mail);
        }
    }

    @DisplayName("메일 상세 조회")
    @Test
    void selectMailDetail() {
        // given
        int mailNo = 1;

        // when
        MailDTO mailDetail = mailService.selectMailDetail(mailNo);

        // then
        Assertions.assertNotNull(mailDetail);
        System.out.println(mailNo + "번 메일 : " + mailDetail);
    }

    @DisplayName("발송 취소")
    @ParameterizedTest
    @CsvSource("3")
    void cancelSendMail(int mailNo) {
        // given

        // when
        int result = mailService.cancelSendMail(mailNo);

        // then
        Assertions.assertNotEquals(result, 0);
    }

    @DisplayName("보낸 메일 삭제")
    @ParameterizedTest
    @CsvSource("2, 240425003")
    void deleteSendMail(int mailNo, int senderMem) {
        // given

        // when
        int result = mailService.deleteSendMail(mailNo, senderMem);

        // then
        Assertions.assertNotEquals(result , 0);
    }

    @DisplayName("받은 메일 삭제")
    @ParameterizedTest
    @CsvSource({"3, 240425002"})
    void deleteReceiveMail(int mailNo, int receiverMem) {
        // given

        // when
        int result = mailService.deleteReceiveMail(mailNo, receiverMem);

        // then
        Assertions.assertNotEquals(result, 0);
    }

    @DisplayName("답장")
    @Test
    void replyMail() {
        // given

        // when

        // then
    }
}