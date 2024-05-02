package com.devsplan.ketchup.mail.controller;

import com.devsplan.ketchup.mail.dto.MailDTO;
import com.devsplan.ketchup.mail.dto.ReceiverDTO;
import com.devsplan.ketchup.mail.service.MailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultHandler;

import java.util.ArrayList;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.any;
import static org.awaitility.Awaitility.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MailController.class)
public class MailControllerTests {
    @Autowired
    MockMvc mvc;

    @MockBean
    MailService mailService;

    @Autowired
    ObjectMapper mapper;

    @DisplayName("메일 등록")
    @Test
    void insertMail() throws Exception {
        // given
        MailDTO testMail = new MailDTO();
        testMail.setSenderMem("1");
        testMail.setMailTitle("컨트롤러 테스트1");
        testMail.setMailContent("메일 내용1");
        testMail.setSendCancelStatus('N');
        testMail.setSendDelStatus('N');

        List<ReceiverDTO> testReceiver = new ArrayList<>();
        ReceiverDTO testReceiver1 = new ReceiverDTO();
        testReceiver1.setMailNo(testMail.getMailNo());
        testReceiver1.setReceiverMem("2");
        testReceiver1.setReceiverDelStatus('N');

        testReceiver.add(testReceiver1);

        testMail.setReceivers(testReceiver);

        // when

        // then
        mvc.perform(post("/mails")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(testMail))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mailNo").exists())
                .andExpect(jsonPath("$.sendMailTime").exists())
                .andDo(print());
    }

    @DisplayName("보낸 메일 조회")
    @Test
    void selectSendMail() throws Exception {
        this.mvc.perform(get("/mails?part=send"))
                .andExpect(status().isOk())
                .andDo(System.out::println);
    }

    @DisplayName("받은 메일 조회")
    @Test
    void selectReceiverMail() throws Exception {
        this.mvc.perform(get("/mails?part=receiver"))
                .andExpect(status().isOk())
                .andDo(System.out::println);
    }

//    @DisplayName("답장")
//    @Test
//    void replyMail() {
//
//    }
}