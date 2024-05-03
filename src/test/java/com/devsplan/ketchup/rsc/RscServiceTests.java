package com.devsplan.ketchup.rsc;

import com.devsplan.ketchup.reserve.dto.ResourceDTO;
import com.devsplan.ketchup.rsc.controller.RscController;
import com.devsplan.ketchup.rsc.dto.RscDTO;
import com.devsplan.ketchup.rsc.service.RscService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.stream.Stream;

@SpringBootTest
public class RscServiceTests {
    @Autowired
    RscService rscService;

    private static Stream<Arguments> getReserveInfo() {
        return Stream.of(
//                Arguments.of(
//                        LocalDateTime.of(2024, 5, 1, 13, 30),
//                        LocalDateTime.of(2024, 5, 1, 18, 0),
//                        1,
//                        "위클리 미팅",
//                        "회의실",
//                        "회의실 A",
//                        "별관 3층 301호",
//                        10,
//                        true,
//                        "5월 첫째주 위클리 미팅"
//                ),
//                Arguments.of(
//                        LocalDateTime.of(2024, 5, 2, 10, 0),
//                        LocalDateTime.of(2024, 5, 2, 12, 0),
//                        2,
//                        "물류 창고 방문",
//                        "법인차량",
//                        "황금마티즈",
//                        "본관 지하 1층 주차장 B20 영역",
//                        4,
//                        true,
//                        "물류 창고 방문을 위한 법인 차량 대여"
//                ),
//                Arguments.of(
//                        LocalDateTime.of(2024, 5, 3, 15, 0),
//                        LocalDateTime.of(2024, 5, 3, 17, 0),
//                        3,
//                        "진급식",
//                        "회의실",
//                        "회의실 B",
//                        "본관 4층 401호",
//                        8,
//                        true,
//                        "이대리 등 30명 진급 축하식을 위한 진급식 진행"
//                ),
                Arguments.of(
                        LocalDateTime.of(2024, 5, 3, 17, 0),
                        LocalDateTime.of(2024, 5, 3, 17, 30),
                        3,
                        "미국 바이어와 화상 미팅",
                        "회의실",
                        "회의실 B",
                        "본관 4층 401호",
                        8,
                        true,
                        "미국 바이어와 2024년 Q4 런칭 제품의 선박 일정 관련 줌 미팅"
                )
        );
    }

    @DisplayName("자원 등록 테스트")
    @ParameterizedTest
    @MethodSource("getReserveInfo")
    public void insertResourceTest(int rscNo, String rscCategory, String rscName, String rscInfo, int rscCap, boolean rscIsAvailable, String rscDescr) {
        // given
        RscDTO rscDTO = new RscDTO(rscNo, rscCategory, rscName, rscInfo, rscCap, rscIsAvailable, rscDescr);

        // when & then
        Assertions.assertDoesNotThrow(
                () -> rscService.insertResource(rscDTO)
        );
        System.out.println("등록한 자원 내용 = " + rscDTO);
    }

    @DisplayName("자원 등록 조회")
    @Test
    public void selectResource() {

    }
}
