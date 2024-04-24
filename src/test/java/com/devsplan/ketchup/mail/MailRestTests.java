package com.devsplan.ketchup.mail;

import com.devsplan.ketchup.mail.dto.MailDTO;
import com.devsplan.ketchup.mail.dto.MailFileDTO;
import com.devsplan.ketchup.mail.dto.ReceiverDTO;
import com.devsplan.ketchup.mail.entity.Mail;
import com.devsplan.ketchup.mail.entity.MailFile;
import com.devsplan.ketchup.mail.service.MailService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@SpringBootTest
public class MailRestTests {
    @Autowired
    private MailService mailService;

    private static Stream<Arguments> getMail() {
        return Stream.of(
                Arguments.of(
                        1,
                        "240423-001",
                        "[자료 요청] 1분기 실적보고서 내 영업실 데이터",
                        "지난번 공유주신 1분기 실적보고서는 잘 받았습니다. 리포터에 산입하신 영업팀의 지난분기 계약 및 실적 데이터 전달을 부탁드리겠습니다.",
                        'N',
                        'N'
                )
        );
    }

    @DisplayName("메일 전송")
    @ParameterizedTest
    @MethodSource("getMail")
    void insertMail(int mailNo, String sender, String mailTitle, String mailContent, char sendCancelStatus, char sendDelStatus) {
        // given
        MailDTO newMail = new MailDTO(
                mailNo, sender, mailTitle, mailContent, sendCancelStatus, sendDelStatus
        );

        // when

        // then
        Assertions.assertDoesNotThrow(
                () -> mailService.insertMail(newMail)
        );

        // -----------------------------------------------------------------------------------------
        // 파일 - 임시 경로를 만들어줘서 메소드를 불러온다...??
        // 메일, 수신자 서비스 메소드를 2개를 여기에 불러온다..???

        // given
        // 메일
        /*MailDTO mailInfo = new MailDTO(
                mailNo, sender, mailTitle, mailContent, sendCancelStatus, sendDelStatus
        );

        List<ReceiverDTO> receiverList = new ArrayList<>();
        ReceiverDTO receiverInfo = new ReceiverDTO(
                receiverNo, mailNo, receiverName, readTime, receiverDelStatus
        );
        receiverList.add(receiverInfo);

        // when

        // then
        Assertions.assertDoesNotThrow(
                () -> mailService.insertMail(mailInfo)
        );*/
    }

    @DisplayName("메일 목록 조회")
    @ParameterizedTest
    @MethodSource("getMail")
    void selectMailList() {
        // given

        // when
        List<Mail> mailList = mailService.selectMailList("240423-001");

        // then
        Assertions.assertNotNull(mailList);
        for(Mail mail : mailList) {
            System.out.println("240424-001 메일 목록 : " + mail);
        }
    }
}