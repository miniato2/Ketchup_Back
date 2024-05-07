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
import java.util.List;
import java.util.stream.Stream;

@SpringBootTest
public class RscServiceTests {
    /* 자원 관리팀만 등록, 조회, 수정, 삭제가 가능함 */

    @Autowired
    RscService rscService;

    private static Stream<Arguments> getReserveInfo() {
        return Stream.of(
                Arguments.of(
                        "회의실",
                        "회의실A",
                        "7층 706호",
                        8,
                        true,
                        null
                ),
                Arguments.of(
                        "회의실",
                        "회의실B",
                        "7층 707호",
                        6,
                        true,
                        null
                ),
                Arguments.of(
                        "차량",
                        "EQ900",
                        "11가 1111",
                        5,
                        true,
                        null
                ),
                Arguments.of(
                        "차량",
                        "아반떼",
                        "11가 2222",
                        5,
                        true,
                        null
                )
        );
    }

    @DisplayName("자원 등록 테스트")
    @ParameterizedTest
    @MethodSource("getReserveInfo")
    public void insertResourceTest(String rscCategory, String rscName, String rscInfo, int rscCap, boolean rscIsAvailable, String rscDescr) {
        // given
        RscDTO rscDTO = new RscDTO(rscCategory, rscName, rscInfo, rscCap, rscIsAvailable, rscDescr);

        // when & then
        Assertions.assertDoesNotThrow(
                () -> rscService.insertResource(rscDTO)
        );
        System.out.println("등록한 자원 내용 = " + rscDTO);
    }

    @DisplayName("자원 등록 조회")
    @Test
    public void selectResource() {
        // given
        String part = "회의실";

        // when
        List<RscDTO> rscList = rscService.selectRscList(part);

        // then
        Assertions.assertNotNull(rscList);
        System.out.println("자원 목록 조회");
        System.out.println(rscList);
    }

    @DisplayName("자원 상세 조회")
    @Test
    public void selectResourceDetail() {
        // given
        int rscNo = 15;

        // when
        RscDTO rscDetail = rscService.selectResourceDetail(rscNo);

        // then
        Assertions.assertNotNull(rscDetail);
        System.out.println("자원 상세 조회");
        System.out.println(rscDetail);
    }

    @DisplayName("자원 수정")
    @Test
    public void updateResource() {
        // given
        int rscNo = 2;

        RscDTO updateRsc = new RscDTO(
                true,
                "비고 수정했습니다."
        );

        // when

        // then
        Assertions.assertDoesNotThrow(() -> rscService.updateResource(rscNo, updateRsc));
    }

    @DisplayName("자원 삭제")
    @Test
    public void deleteResource() {
        // given
        int rscNo = 16;

        // when
        int result = rscService.deleteResource(rscNo);

        // then
        Assertions.assertEquals(1, result);
    }
}
