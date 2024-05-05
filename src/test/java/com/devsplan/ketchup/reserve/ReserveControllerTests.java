package com.devsplan.ketchup.reserve;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class ReserveControllerTests {

    @Autowired
    private MockMvc mvc;

    // 토큰 넣어서 전역에서 사용
    private final String token = "Bearer eyJkYXRlIjoxNzE0Njk3NjEzNDk0LCJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJwb3NpdGlvbk5hbWUiOiLtjIDsnqUiLCJkZXBObyI6NSwibWVtYmVyTm8iOiI1IiwicG9zaXRpb25MZXZlbCI6Miwic3ViIjoia2V0Y2h1cCB0b2tlbiA6IDUiLCJyb2xlIjoiTFYyIiwicG9zaXRpb25TdGF0dXMiOiJZIiwicG9zaXRpb25ObyI6MiwiZXhwIjoxNzE0Nzg0MDEzfQ.8oTTf5UEsIZa4-CegR-IdKLJqj8MGv7-RdCvSZqXNsA";

    private RequestBuilder request;


    @DisplayName("자원 예약 목록 조회 컨트롤러 테스트")
    @Test
    void selectReserveList() throws Exception {
        // given
        String rscCategory = "회의실";
        LocalDate rsvDate = LocalDate.of(2024, 5, 4);
        String formattedDate = rsvDate.toString();

        // when
        request = MockMvcRequestBuilders.get("/reserves?category=" + rscCategory + "&rsvDate=" + formattedDate).header("Authorization", token);

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

}
