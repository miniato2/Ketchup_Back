package com.devsplan.ketchup.schedule;

import com.devsplan.ketchup.schedule.service.ScheduleService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ScheduleRestTests {

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private ScheduleService scheduleService;

    @DisplayName("부서별 일정 목록 조회")
    @Test
    void selectScheduleList() {
        // /department/{departmentno}/schedules?category={filtercategory}

        // given

        // when

        // then
    }

    @DisplayName("부서별 일정 상세 조회")
    @Test
    void selectScheduleDetail() {
        // /department/{departmentno}/schedules/{scheduleno}

        // given

        // when

        // then
    }

    @DisplayName("부서별 일정 등록")
    @Test
    void insertSchedule() {
        // /members

        // given

        // when

        // then
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
