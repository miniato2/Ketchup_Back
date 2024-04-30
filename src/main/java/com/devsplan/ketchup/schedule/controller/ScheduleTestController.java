//package com.devsplan.ketchup.schedule.controller;
//
//import com.devsplan.ketchup.schedule.dto.ScheduleDTO;
//import com.devsplan.ketchup.schedule.service.ScheduleService;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//public class ScheduleTestController {
//
//    @Mock
//    private ScheduleService scheduleService;
//
//    @InjectMocks
//    private ScheduleController scheduleController;
//
//    private MockMvc mockMvc;
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(scheduleController).build();
//    }
//
//    @DisplayName("부서별 일정 목록 조회")
//    @Test
//    void selectScheduleList() throws Exception {
//        int dptNo = 1;
//
//        List<ScheduleDTO> scheduleList = new ArrayList<>();
//        scheduleList.add(new ScheduleDTO());
//
//        when(scheduleService.selectScheduleList(dptNo)).thenReturn(scheduleList);
//
//        mockMvc.perform(get("/schedules/department/{dptNo}", dptNo)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].skdNo").exists());
//    }
//
//    @DisplayName("부서별 일정 상세 조회")
//    @Test
//    void selectScheduleDetail() throws Exception {
//        int dptNo = 1;
//        int skdNo = 1;
//
//        ScheduleDTO schedule = new ScheduleDTO();
//        schedule.setSkdNo(skdNo);
//
//        List<ScheduleDTO> scheduleList = new ArrayList<>();
//        scheduleList.add(schedule);
//
//        when(scheduleService.selectScheduleDetail(dptNo, skdNo)).thenReturn(scheduleList);
//
//        mockMvc.perform(get("/schedules/department/{dptNo}/schedules/{skdNo}", dptNo, skdNo)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.schedule.skdNo").value(skdNo));
//    }
//
//}
