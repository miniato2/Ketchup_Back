package com.devsplan.ketchup.schedule;

import com.devsplan.ketchup.schedule.dto.DepartmentDTO;
import com.devsplan.ketchup.schedule.dto.ScheduleDTO;
import com.devsplan.ketchup.schedule.service.ScheduleService;
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
public class ScheduleRestTests {

    @Autowired
    private ScheduleService scheduleService;

    @DisplayName("부서별 일정 목록 조회")
    @Test
    void selectScheduleList() {
        // given
        int dptNo = 1;

        // when
        List<ScheduleDTO> foundSchedule = scheduleService.selectScheduleList(dptNo);

        // then
        Assertions.assertNotNull(foundSchedule);
        System.out.println("부서별 일정 목록 조회 = " + foundSchedule);

    }

    @DisplayName("부서별 일정 상세 조회")
    @Test
    void selectScheduleDetail() {
        // given
        int dptNo = 1;
        int skdNo = 2;

        // when
        List<ScheduleDTO> foundSchedule = scheduleService.selectScheduleDetail(dptNo, skdNo);

        // then
        Assertions.assertNotNull(foundSchedule);
        System.out.println("부서별 일정 상세 조회 = " + foundSchedule);
    }

    private static Stream<Arguments> getScheduleInfo() {
        return Stream.of(
                Arguments.of(3, 1, "신규 등록한 일정1", LocalDateTime.of(2024, 6, 23, 10, 0), LocalDateTime.of(2024, 6, 23, 23, 0), "신규 위치", "신규 등록 일정이 정상적으로 반영되었습니다.")
        );
    }

    @DisplayName("부서별 일정 등록")
    @ParameterizedTest
    @MethodSource("getScheduleInfo")
    void insertSchedule(int skdNo, int dptNo, String skdName, LocalDateTime skdStartDttm, LocalDateTime skdEndDttm, String skdLocation, String skdMemo) {
        // given
        ScheduleDTO newSchedule = new ScheduleDTO(
                skdNo,
                new DepartmentDTO(dptNo),
                skdName,
                skdStartDttm,
                skdEndDttm,
                skdLocation,
                skdMemo
        );

        // when


        // then
        Assertions.assertDoesNotThrow(
                () -> scheduleService.insertSchedule(newSchedule)
        );
    }

    @DisplayName("부서별 일정 수정")
    @Test
    void updateSchedule() {
        // /schedules/{scheduleno}

        // given

        // when

        // then
    }

    @DisplayName("부서별 일정 삭제")
    @Test
    void deleteSchedule() {
        // /schedules/{scheduleno}

        // given

        // when

        // then
    }

}
