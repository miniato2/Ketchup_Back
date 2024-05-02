package com.devsplan.ketchup.reserve;

import com.devsplan.ketchup.reserve.dto.ReserveDTO;
import com.devsplan.ketchup.reserve.dto.ResourceDTO;
import com.devsplan.ketchup.reserve.entity.Resource;
import com.devsplan.ketchup.reserve.service.ReserveService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@SpringBootTest
public class ReserveServiceTests {

    @Autowired
    private ReserveService reserveService;

    @DisplayName("자원 예약 목록 조회")
    @Test
    void selectReserveList() {
        // given
        String rscCategory = "회의실";
        LocalDate rsvDate = LocalDate.of(2024, 5, 1);

        // when
        List<ReserveDTO> foundReserve = reserveService.selectReserveList(rscCategory, rsvDate);

        // then
        Assertions.assertNotNull(foundReserve);
        System.out.println("자원 예약 목록 조회 = " + foundReserve);
    }

    @DisplayName("자원 예약 상세 조회")
    @Test
    void selectReserveDetail() {
        // given
        int rsvNo = 3;

        // when
        ReserveDTO foundReserve = reserveService.selectReserveDetail(rsvNo);

        // then
        Assertions.assertNotNull(foundReserve);
        System.out.println("자원 예약 상세 조회 = " + foundReserve);
    }

    private static Stream<Arguments> getResourceInfo() {
        return Stream.of(
                Arguments.of(
                        1,
                        "회의실",
                        "회의실 A",
                        "별관 3층 301호",
                        10,
                        true,
                        "Smart TV, 빔프로젝터, 책상, 의자 비치"
                ),
                Arguments.of(
                        2,
                        "법인차량",
                        "황금마티즈",
                        "본관 지하 1층 주차장 B20 영역",
                        4,
                        true,
                        "4인용 소형 차량"
                ),
                Arguments.of(
                        3,
                        "회의실",
                        "회의실 B",
                        "본관 4층 401호",
                        20,
                        true,
                        "Smart TV, 빔프로젝터, 책상, 의자, 단상 비치, 경복궁 뷰"
                )
        );
    }

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
    @MethodSource("getResourceInfo")
    public void insertResourceTest(int rscNo, String rscCategory, String rscName, String rscInfo, int rscCap, boolean rscIsAvailable, String rscDescr) {
        // given
        ResourceDTO resourceDTO = new ResourceDTO(rscNo, rscCategory, rscName, rscInfo, rscCap, rscIsAvailable, rscDescr);

        // when & then
        Assertions.assertDoesNotThrow(
                () -> reserveService.insertResource(resourceDTO)
        );
        System.out.println("등록한 자원 내용 = " + resourceDTO);
    }

    @DisplayName("자원 예약 등록 테스트")
    @ParameterizedTest
    @MethodSource("getReserveInfo")
    public void insertReserveTest(LocalDateTime rsvStartDttm, LocalDateTime rsvEndDttm, int rscNo, String rsvDescr, String rscCategory, String rscName, String rscInfo, int rscCap, boolean rscIsAvailable, String rscDescr) {
        // given
        ResourceDTO resourceDTO = new ResourceDTO(rscNo, rscCategory, rscName, rscInfo, rscCap, rscIsAvailable, rscDescr);
        ReserveDTO newReserve = new ReserveDTO(
                rsvDescr,
                rsvStartDttm,
                rsvEndDttm,
                resourceDTO
        );

        // when
        Assertions.assertDoesNotThrow(
                () -> {
                    Resource resource = reserveService.getResourceFromDatabase(rscNo);
                    reserveService.insertReserve(newReserve, resource);
                }
        );

        // then
        System.out.println("등록한 예약 내용 = " + newReserve);
    }


//    @Transactional
//    @ParameterizedTest
//    @MethodSource("updatedReserveInfo")
//    void updateReserve(ReserveDTO updateReserve) {
//        // given
//
//        // when
//
//        // then
//    }
}
