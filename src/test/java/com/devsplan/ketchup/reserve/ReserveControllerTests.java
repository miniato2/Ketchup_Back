package com.devsplan.ketchup.reserve;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class ReserveControllerTests {

    @Autowired
    private MockMvc mvc;

    // 토큰 넣어서 전역에서 사용
    private final String token = "Bearer eyJkYXRlIjoxNzE1MTMwMjc4MjI4LCJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJwb3NpdGlvbk5hbWUiOiLtjIDsnqUiLCJkZXBObyI6NSwibWVtYmVyTm8iOiI1IiwicG9zaXRpb25MZXZlbCI6Miwic3ViIjoia2V0Y2h1cCB0b2tlbiA6IDUiLCJyb2xlIjoiTFYyIiwicG9zaXRpb25TdGF0dXMiOiJZIiwicG9zaXRpb25ObyI6MiwiZXhwIjoxNzE1MjE2Njc4fQ.4KtDg3lZ7bdgJWSpEY6tNkqB-cQYRcI8kwncwqYBKMc";

    private RequestBuilder request;


    @DisplayName("자원 예약 목록 조회 컨트롤러 테스트")
    @Test
    void selectReserveList() throws Exception {
        // given
        String rscCategory = "회의실";
        LocalDate rsvDate = LocalDate.of(2024, 5, 4);
        String formattedDate = rsvDate.toString();

        // when
        request = get("/reserves?category=" + rscCategory + "&rsvDate=" + formattedDate).header("Authorization", token);

        // then
        mvc.perform(request)
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("자원 예약 상세 조회 컨트롤러 테스트")
    @Test
    void selectReserveDetail() throws Exception {
        // given
        int rsvNo = 6;

        // when
        request = get("/reserves/" + rsvNo).header("Authorization", token);

        // then
        mvc.perform(request)
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("자원 예약 등록 컨트롤러 테스트")
    @Test
    void insertReserve() throws Exception {
        // given
        String jsonbody = "{ " +
                "\"rsvStartDttm\": \"2024-05-05 오후 3시 0분\", " +
                "\"rsvEndDttm\": \"2024-05-05 오후 4시 30분\", " +
                "\"rsvDescr\": \"ReserveControllerTests에서 등록한 예약건\", " +
                "\"resources\": { " +
                "\"rscNo\": 3, " +
                "\"rscCategory\": \"회의실\", " +
                "\"rscName\": \"회의실 B\", " +
                "\"rscInfo\": \"본관 4층 401호\", " +
                "\"rscCap\": 20, " +
                "\"rscIsAvailable\": true, " +
                "\"rscDescr\": \"Smart TV, HDMI, 마이크, 스피커, 빔프로젝터, 의자 비치\" " +
                "}" +
                "}";

        // when
        request = MockMvcRequestBuilders.post("/reserves")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonbody)
                .header("Authorization", token);

        // then
        mvc.perform(request)
                .andExpect(status().isCreated());
    }

    @DisplayName("자원 예약 수정 컨트롤러 테스트")
    @Test
    void updateReserve() throws Exception {
        // given
        int rsvNo = 6;

        String jsonbody = "{" +
                "\"rsvDescr\": \"rsvNo 6 예약건 수정의 건\"," +
                "\"rsvStartDttm\": \"2024-05-08 오후 1시 0분\"," +
                "\"rsvEndDttm\": \"2024-05-08 오후 1시 30분\"" +
                "}";

        // when
        MvcResult result = mvc.perform(MockMvcRequestBuilders.put("/reserves/" + rsvNo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonbody)
                        .header("Authorization", token))
                .andReturn();

        // then
        int status = result.getResponse().getStatus();
        Assertions.assertEquals(200, status);
    }

    @DisplayName("자원 예약 삭제 컨트롤러 테스트")
    @Test
    void deleteReserve() throws Exception {
        // given
        int rsvNo = 6;

        // when
         MvcResult result = mvc.perform(MockMvcRequestBuilders.delete("/reserves/" + rsvNo)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", token))
                                .andReturn();

        // then
        int status = result.getResponse().getStatus();
        Assertions.assertEquals(204, status);
    }

    }
