package com.devsplan.ketchup.schedule;

import com.devsplan.ketchup.schedule.controller.ResponseMessage;
import com.devsplan.ketchup.schedule.dto.DepartmentDTO;
import com.devsplan.ketchup.schedule.dto.ScheduleDTO;
import com.devsplan.ketchup.schedule.entity.Schedule;
import com.devsplan.ketchup.schedule.repository.ScheduleRepository;
import com.devsplan.ketchup.schedule.service.ScheduleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
//@AutoConfigureMockMvc
public class ScheduleRestTests {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private ScheduleRepository scheduleRepository;

//    @Autowired
//    private MockMvc mvc;

//    @DisplayName("부서별 일정 목록 조회")
//    @Test
//    void selectScheduleList() throws Exception {
//        int dptNo = 1;
//
//        List<ScheduleDTO> schedules = List.of(
//                new ScheduleDTO(1, new DepartmentDTO(1), "해외 바이어 미팅", LocalDateTime.now(), LocalDateTime.now(), "일정의 위치 정보", "일정의 메모")
//        );
//
//        when(scheduleService.selectScheduleList(dptNo)).thenReturn(schedules);
//
//        mvc.perform(MockMvcRequestBuilders
//                        .get("/schedules/department/{dptNo}", dptNo)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].skdNo").exists());
//    }


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
                Arguments.of(2, 1, "신규 등록한 일정2", LocalDateTime.of(2024, 6, 23, 10, 0), LocalDateTime.of(2024, 6, 23, 23, 0), "신규 위치", "신규 등록 일정이 정상적으로 반영되었습니다.")
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
    @ParameterizedTest
    @MethodSource("getScheduleInfo")
    void updateSchedule(int skdNo, int dptNo, String skdName, LocalDateTime skdStartDttm, LocalDateTime skdEndDttm, String skdLocation, String skdMemo) {
        // given
        ScheduleDTO updateSchedule = new ScheduleDTO(
                2,
                new DepartmentDTO(dptNo),
                "수정된 일정4",
                LocalDateTime.of(2025, 6, 23, 10, 0),
                skdEndDttm,
                "수정된 위치4",
                "수정된 메모4"
        );

        // when
        Assertions.assertDoesNotThrow(() -> scheduleService.updateSchedule(updateSchedule));
    }

    @DisplayName("부서별 일정 삭제")
    @Test
    void deleteSchedule() {
        // /schedules/{scheduleno}

        // given
        int skdNo = 2;
        Long skdNoLong = (long) skdNo;

        // when
        scheduleService.deleteById(skdNo);

        // then
        Assertions.assertNull(scheduleRepository.findById(skdNoLong).orElse(null));
    }

}
