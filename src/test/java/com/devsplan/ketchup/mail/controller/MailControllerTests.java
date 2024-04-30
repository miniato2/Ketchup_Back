package com.devsplan.ketchup.mail.controller;

import com.devsplan.ketchup.mail.dto.MailDTO;
import com.devsplan.ketchup.mail.dto.ReceiverDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MailControllerTests {
    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mvc;

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
        this.mvc.perform(post("/mails")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testMail))
                )
                .andExpect(status().isOk())
                .andDo(System.out::println);
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
}
