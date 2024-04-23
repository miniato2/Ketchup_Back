package com.devsplan.ketchup.schedule.controller;

import com.devsplan.ketchup.schedule.dto.DepartmentDTO;
import com.devsplan.ketchup.schedule.dto.ScheduleDTO;
import com.devsplan.ketchup.schedule.service.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
@Slf4j
public class ScheduleController {

    private final ScheduleService scheduleService;
    private List<ScheduleDTO> schedule;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
        schedule = new ArrayList<>();

        schedule.add(new ScheduleDTO(1, new DepartmentDTO(1), "해외바이어 미팅", "2024-04-22 오후 4시 30분", "2024-04-22 오후 7시", "코스트코 광명점 4층 ㅂㅈㅎ 부장실", "Q4 제안서, 제안 샘플 20종 지참"));
    }

    // 부서별 일정 목록 조회
    @GetMapping("/department/{dptNo}")
    public ResponseEntity<ResponseMessage> selectScheduleList(@PathVariable int dptNo) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        ScheduleDTO foundSchedule = null;
        for (int i = 0; i < schedule.size(); i++) {
            ScheduleDTO scheduleDTO = schedule.get(i);
            if (scheduleDTO.getDptNo().getDptNo() == dptNo) {
                foundSchedule = scheduleDTO;
                break;
            }
        }

        if (foundSchedule != null) {
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("schedule", foundSchedule);
            return ResponseEntity.ok().headers(headers)
                    .body(new ResponseMessage(200, "조회 성공", responseMap));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage(404, "일정을 찾을 수 없습니다.", null));
        }
    }

    // 부서별 일정 상세 조회
    @GetMapping("/department/{dptNo}/schedules/{skdNo}")
    public ResponseEntity<ResponseMessage> selectScheduleDetail(@PathVariable int dptNo, @PathVariable int skdNo) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        ScheduleDTO foundSchedule = null;
        for (int i = 0; i < schedule.size(); i++) {
            ScheduleDTO scheduleDTO = schedule.get(i);
            if (scheduleDTO.getDptNo().getDptNo() == dptNo && scheduleDTO.getSkdNo() == skdNo) {
                foundSchedule = scheduleDTO;
                break;
            }
        }

        if (foundSchedule != null) {
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("schedule", foundSchedule);
            return ResponseEntity.ok().headers(headers)
                    .body(new ResponseMessage(200, "조회 성공", responseMap));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage(404, "일정을 찾을 수 없습니다.", null));
        }
    }

    // 부서별 일정 등록
    @PostMapping("/schedules")
    public ResponseEntity<?> insertSchedule(@RequestBody ScheduleDTO newSchedule) {
        System.out.println("newSchedule = " + newSchedule);

        int lastSkdNo = schedule.get(schedule.size() -1).getSkdNo();
        newSchedule.setSkdNo(lastSkdNo + 1);

        schedule.add(newSchedule);

        return ResponseEntity.created(URI.create("/schedules/" + schedule.get(schedule.size() - 1).getSkdNo())).build();
    }



}