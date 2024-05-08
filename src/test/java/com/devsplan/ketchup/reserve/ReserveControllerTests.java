package com.devsplan.ketchup.reserve;

import com.devsplan.ketchup.reserve.dto.ReserveDTO;
import com.devsplan.ketchup.reserve.service.ReserveService;
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
import java.time.format.DateTimeFormatter;

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

    @Autowired
    private ReserveService reserveService;

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
                "\"reserver\": \"3\", " +
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
        String updatedDescr = "rsvNo 6 예약건 수정의 건";
        LocalDateTime updatedStartDttm = LocalDateTime.of(2024, 5, 8, 13, 0);
        LocalDateTime updatedEndDttm = LocalDateTime.of(2024, 5, 8, 13, 30);

        String jsonbody = "{" +
                "\"rsvDescr\": \"" + updatedDescr + "\"," +
                "\"rsvStartDttm\": \"" + updatedStartDttm.format(DateTimeFormatter.ofPattern("yyyy-MM-dd a h시 m분")) + "\"," +
                "\"rsvEndDttm\": \"" + updatedEndDttm.format(DateTimeFormatter.ofPattern("yyyy-MM-dd a h시 m분")) + "\"" +
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

        // 수정된 예약 정보를 다시 조회하여 검증
        ReserveDTO updatedReserve = reserveService.selectReserveDetail(rsvNo);
        Assertions.assertNotNull(updatedReserve);
        Assertions.assertEquals(updatedDescr, updatedReserve.getRsvDescr());
        Assertions.assertEquals(updatedStartDttm, updatedReserve.getRsvStartDttm());
        Assertions.assertEquals(updatedEndDttm, updatedReserve.getRsvEndDttm());
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
